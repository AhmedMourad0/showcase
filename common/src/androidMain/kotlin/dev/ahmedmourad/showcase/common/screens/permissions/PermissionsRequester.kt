package dev.ahmedmourad.showcase.common.screens.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import dev.ahmedmourad.showcase.common.R
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.VerticalSpacing
import dev.ahmedmourad.showcase.common.initializers.appCtx
import dev.ahmedmourad.showcase.common.utils.LifecycleEffect
import dev.ahmedmourad.showcase.common.utils.LocalActivity
import dev.ahmedmourad.showcase.common.utils.openAppOverlaySettings
import dev.ahmedmourad.showcase.common.utils.openAppSettings
import dev.ahmedmourad.showcase.common.utils.openWriteAppSettings

//TODO: turn this into a library, the pagination utils code as well
@Immutable
sealed interface PermissionState {
    @Immutable
    data object Granted : PermissionState
    @Immutable
    data object Unknown : PermissionState
    @Immutable
    sealed interface Declined : PermissionState {
        val showRationale: Boolean
    }
    @Immutable
    data class DeclinedOnce(override val showRationale: Boolean) : Declined
    @Immutable
    data class DeclinedPermanently(override val showRationale: Boolean) : Declined
}

@Immutable
interface PermissionStrategy {
    fun isGranted(activity: Activity): Boolean
    fun wasDeclinedOnce(activity: Activity): Boolean
    fun gotoSettings(activity: Activity)
}

@Immutable
data class DefaultPermissionStrategy(private val id: String) : PermissionStrategy {
    override fun isGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(appCtx, this.id) == PackageManager.PERMISSION_GRANTED
    }
    override fun wasDeclinedOnce(activity: Activity): Boolean {
        return activity.shouldShowRequestPermissionRationale(this.id)
    }
    override fun gotoSettings(activity: Activity): Unit = openAppSettings()
}

@Immutable
object SystemAlertWindowPermissionStrategy : PermissionStrategy {
    override fun isGranted(activity: Activity): Boolean {
        return Settings.canDrawOverlays(activity)
    }
    override fun wasDeclinedOnce(activity: Activity): Boolean = false
    override fun gotoSettings(activity: Activity): Unit = openAppOverlaySettings()
}

@Immutable
object WriteSystemSettingsPermissionStrategy : PermissionStrategy {
    override fun isGranted(activity: Activity): Boolean {
        return Settings.System.canWrite(activity)
    }
    override fun wasDeclinedOnce(activity: Activity): Boolean = false
    override fun gotoSettings(activity: Activity): Unit = openWriteAppSettings()
}

fun PermissionStrategy.findState(activity: Activity): PermissionState {
    return when {
        isGranted(activity) -> PermissionState.Granted
        wasDeclinedOnce(activity) -> PermissionState.DeclinedOnce(true)
        else -> PermissionState.DeclinedPermanently(true)
    }
}

//TODO: initialize state here using the strategy and only trigger the launcher
// in the requester if any of the permissions are declined .. basically eliminating the unknown state
@Immutable
data class Permission(
    val id: String,
    val state: PermissionState = PermissionState.Unknown,
    val showRationaleFirst: Boolean = false,
    val strategy: PermissionStrategy = DefaultPermissionStrategy(id)
)

@Immutable
data class RationaleContentInput(
    val permissions: List<Permission>,
    val areDeclinedOnce: Boolean,
    val onDismiss: () -> Unit,
    val onAction: PermissionDialogOnActionScope.() -> Unit
)

@Immutable
data class PermissionDialogOnActionScope(
    val onDismiss: () -> Unit
)

@Immutable
sealed interface RationaleProvider {
    @Immutable
    data class PerPermission(val get: (Permission) -> String) : RationaleProvider
    @Immutable
    data class ForAll(val rationale: (List<Permission>) -> String) : RationaleProvider
}

@Composable
fun PermissionRequester(
    value: List<Permission>,
    onValueChange: (List<Permission>) -> Unit,
    onAllGranted: () -> Unit = { },
    rationaleContent: @Composable (RationaleContentInput) -> Unit,
    enabled: Boolean = true,
    grantedContent: @Composable () -> Unit = { }
) {
    @Suppress("NAME_SHADOWING")
    val value by rememberUpdatedState(value)
    val activity = LocalActivity.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        onValueChange(value.map { permission ->
            permission.copy(state = permission.strategy.findState(activity))
        })
    }
    val rationaleFirstPermissions = remember(value) {
        value.filter {
            it.showRationaleFirst && it.state == PermissionState.Unknown
        }.map {
            it.copy(state = it.strategy.findState(activity))
        }.filter { it.state != PermissionState.Granted }
    }
    val declinedPermissions = remember(value) {
        value.takeUnless {
            // we don't want to show our dialog below the system permission dialog
            value.any { it.state == PermissionState.Unknown }
        }?.filter { permission ->
            when (val state = permission.state) {
                PermissionState.Granted,
                PermissionState.Unknown
                -> false
                is PermissionState.Declined -> state.showRationale
            }
        }.orEmpty()
    }
    if (rationaleFirstPermissions.isNotEmpty()) {
        rationaleContent(RationaleContentInput(
            permissions = rationaleFirstPermissions,
            areDeclinedOnce = true,
            onDismiss = {
                onValueChange(value.map { it.copy(showRationaleFirst = false) })
            }, onAction = {
                onDismiss()
                onValueChange(value.map { it.copy(showRationaleFirst = false) })
            }
        ))
    } else if (declinedPermissions.isNotEmpty()) {
        //we show a dialog for declined-once permissions first,
        // once that's handled we show the dialog for declined-permanently permissions
        val hasDeclinedOnce = remember(declinedPermissions) {
            declinedPermissions.any { it.state is PermissionState.DeclinedOnce }
        }
        val dialogPermissions = remember(hasDeclinedOnce, declinedPermissions) {
            declinedPermissions.filter {
                if (hasDeclinedOnce) {
                    it.state is PermissionState.DeclinedOnce
                } else {
                    it.state is PermissionState.DeclinedPermanently
                }
            }
        }
        rationaleContent(RationaleContentInput(
            permissions = dialogPermissions,
            areDeclinedOnce = hasDeclinedOnce,
            onDismiss = {
                onValueChange(value.map { permission ->
                    if (permission in dialogPermissions) {
                        permission.copy(state = when (val state = permission.state) {
                            is PermissionState.DeclinedOnce -> state.copy(showRationale = false)
                            is PermissionState.DeclinedPermanently -> state.copy(showRationale = false)
                            PermissionState.Granted,
                            PermissionState.Unknown, -> permission.state
                        }, showRationaleFirst = false)
                    } else {
                        permission
                    }
                })
            }, onAction = {
                onDismiss()
                if (hasDeclinedOnce) {
                    onValueChange(value.map { permission ->
                        if (permission in dialogPermissions) {
                            permission.copy(state = PermissionState.Unknown, showRationaleFirst = false)
                        } else {
                            permission
                        }
                    })
                } else {
                    dialogPermissions.first().strategy.gotoSettings(activity)
                }
            }
        ))
    }
    LifecycleEffect(Lifecycle.Event.ON_START) {
        //to sync permissions after the user has returned from app settings
        if (value.any { it.state is PermissionState.DeclinedPermanently }) {
            onValueChange(value.map { it.copy(state = PermissionState.Unknown) })
        }
    }
    val unknownPermissions = remember(value) {
        value.filter { it.state == PermissionState.Unknown }
    }
    LaunchedEffect(unknownPermissions, enabled) {
        //we check for unknown permissions as soon as they pop up
        unknownPermissions
            .takeIf { enabled && rationaleFirstPermissions.isEmpty() }
            ?.map { it.id }
            ?.toTypedArray()
            ?.takeIf { it.isNotEmpty() }
            ?.let(launcher::launch)
    }
    val areAllGranted = remember(value) {
        value.all { it.state == PermissionState.Granted }
    }
    LaunchedEffect(areAllGranted) {
        if (areAllGranted) {
            onAllGranted.invoke()
        }
    }
    if (areAllGranted) {
        grantedContent.invoke()
    }
}

@Composable
fun RationaleDialog(
    input: RationaleContentInput,
    rationale: RationaleProvider,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.permissions_required)
) {
    Dialog(onDismissRequest = input.onDismiss) {
        Column(modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            RationaleText(
                title = title,
                input = input,
                rationaleProvider = rationale
            )
            Spacer(Modifier.height(VerticalSpacing))
            Button(
                enabled = Showcase.acceptsInputs,
                onClick =  { input.onAction(PermissionDialogOnActionScope(input.onDismiss)) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ){
                Text(
                    text = stringResource(if (input.areDeclinedOnce) R.string.grant_permissions else R.string.open_app_settings),
                    style = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
fun RationaleContent(
    input: RationaleContentInput,
    rationale: RationaleProvider,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.permissions_required)
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        RationaleText(
            title = title,
            input = input,
            rationaleProvider = rationale
        )
        Spacer(Modifier.height(VerticalSpacing))
        Button(
            enabled = Showcase.acceptsInputs,
            onClick =  { input.onAction(PermissionDialogOnActionScope(input.onDismiss)) }
        ){
            Text(
                text = stringResource(if (input.areDeclinedOnce) R.string.grant_permissions else R.string.open_app_settings),
                style = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
private fun RationaleText(
    title: String,
    input: RationaleContentInput,
    rationaleProvider: RationaleProvider,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        val text = remember(rationaleProvider, input.permissions) {
            when (rationaleProvider) {
                is RationaleProvider.ForAll -> rationaleProvider.rationale(input.permissions)
                is RationaleProvider.PerPermission -> input.permissions.joinToString(
                    separator = "\n",
                    transform = { if (input.permissions.size > 1) "- ${rationaleProvider.get(it)}" else rationaleProvider.get(it) },
                    prefix = ""
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(VerticalSpacing))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 15.sp,
                textAlign = TextAlign.Start
            )
        )
    }
}
