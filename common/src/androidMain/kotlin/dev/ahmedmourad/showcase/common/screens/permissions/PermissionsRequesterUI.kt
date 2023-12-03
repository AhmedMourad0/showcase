package dev.ahmedmourad.showcase.common.screens.permissions

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.components.LabelVerticalPadding
import dev.ahmedmourad.showcase.common.compose.theme.DisabledTextOpacity
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.theme.VerticalPadding
import dev.ahmedmourad.showcase.common.initializers.appCtx
import dev.ahmedmourad.showcase.common.utils.LocalActivity
import dev.ahmedmourad.showcase.common.utils.tickerFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

@Composable
fun PermissionsRequesterUI(
    state: PermissionsRequesterState,
    modifier: Modifier = Modifier
) {
    Column(modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = VerticalPadding, start = HorizontalPadding, end = HorizontalPadding)
    ) {
        Spacer(Modifier.height(LabelVerticalPadding))
//        Label(
//            text = "Illustrating the usages of my compose-based permissions requester (check the code):",
//            contentPadding = PaddingValues(bottom = LabelVerticalPadding),
//            modifier = Modifier.padding(horizontal = HorizontalPadding)
//        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PermissionRequesterText("Single Permission", Modifier.weight(1f))
            Spacer(Modifier.width(4.dp))
            PermissionRequesterButton(
                enabled = { state.singlePermission.first().state != PermissionState.Granted },
                onClick = { state.switchTo(RequesterType.Single) }
            )
        }
        PermissionRequester(
            permissions = { state.singlePermission },
            onPermissionsChange = { state.singlePermission = it },
            enabled = { state.enabledRequester == RequesterType.Single },
            rationaleContent = { RationaleDialog { "Rationale for single permission" } }
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PermissionRequesterText("Multiple Permissions", Modifier.weight(1f))
            Spacer(Modifier.width(4.dp))
            PermissionRequesterButton(
                enabled = { state.multiplePermissions.any { it.state != PermissionState.Granted } },
                onClick = { state.switchTo(RequesterType.Multiple) }
            )
        }
        PermissionRequester(
            permissions = { state.multiplePermissions },
            onPermissionsChange = { state.multiplePermissions = it },
            enabled = { state.enabledRequester == RequesterType.Multiple },
            rationaleContent = { RationaleDialog { "Rationale for multiple permissions" } }
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PermissionRequesterText("Custom Strategy", Modifier.weight(1f))
            Spacer(Modifier.width(4.dp))
            PermissionRequesterButton(
                enabled = { state.customPermissions.first().state != PermissionState.Granted },
                onClick = { state.switchTo(RequesterType.Custom) }
            )
        }
        PermissionRequester(
            permissions = { state.customPermissions },
            onPermissionsChange = { state.customPermissions = it },
            enabled = { state.enabledRequester == RequesterType.Custom },
            rationaleContent = { RationaleDialog { "Rationale for custom strategy" } }
        )
        Android8CustomPermissionRefresher(state)
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PermissionRequesterText("Rationale Content", Modifier.weight(1f))
            Spacer(Modifier.width(4.dp))
            PermissionRequesterButton(
                enabled = { state.rationaleContentPermissions.first().state != PermissionState.Granted },
                onClick = { state.switchTo(RequesterType.RationaleContent) }
            )
        }
        PermissionRequester(
            permissions = { state.rationaleContentPermissions },
            onPermissionsChange = { state.rationaleContentPermissions = it },
            enabled = { state.enabledRequester == RequesterType.RationaleContent },
            rationaleContent = { RationaleContent { "Rationale for rationale content" } }
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PermissionRequesterText("Granted Content", Modifier.weight(1f))
            Spacer(Modifier.width(4.dp))
            PermissionRequesterButton(
                enabled = { state.grantedContentPermissions.first().state != PermissionState.Granted },
                onClick = { state.switchTo(RequesterType.GrantedContent) }
            )
        }
        PermissionRequester(
            permissions = { state.grantedContentPermissions },
            onPermissionsChange = { state.grantedContentPermissions = it },
            enabled = { state.enabledRequester == RequesterType.GrantedContent },
            rationaleContent = { RationaleDialog { "Rationale for granted content" } }
        ) {
            Text(text = "Granted!", color = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PermissionRequesterText("On All Granted", Modifier.weight(1f))
            Spacer(Modifier.width(4.dp))
            PermissionRequesterButton(
                enabled = { state.onAllGrantedPermissions.first().state != PermissionState.Granted },
                onClick = { state.switchTo(RequesterType.OnAllGranted) }
            )
        }
        PermissionRequester(
            permissions = { state.onAllGrantedPermissions },
            onPermissionsChange = { state.onAllGrantedPermissions = it },
            enabled = { state.enabledRequester == RequesterType.OnAllGranted },
            rationaleContent = { RationaleDialog { "Rationale for on all granted" } },
            onAllGranted = { Toast.makeText(appCtx, "Granted!", Toast.LENGTH_LONG).show() }
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PermissionRequesterText("Show Rationale First", Modifier.weight(1f))
            Spacer(Modifier.width(4.dp))
            PermissionRequesterButton(
                enabled = { state.rationaleFirstPermissions.first().state != PermissionState.Granted },
                onClick = { state.switchTo(RequesterType.RationaleFirst) }
            )
        }
        PermissionRequester(
            permissions = { state.rationaleFirstPermissions },
            onPermissionsChange = { state.rationaleFirstPermissions = it },
            enabled = { state.enabledRequester == RequesterType.RationaleFirst },
            rationaleContent = { RationaleDialog { "Rationale for show rationale first" } }
        )
    }
}

@Composable
private fun PermissionRequesterButton(
    enabled: () -> Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentEnabled = enabled()
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        enabled = currentEnabled,
        modifier = modifier
    ) {
        Text(text = "Start",
            color = MaterialTheme.colorScheme.onPrimary.copy(
                alpha = if (currentEnabled) 1f else DisabledTextOpacity
            )
        )
    }
}

@Composable
private fun PermissionRequesterText(text: String, modifier: Modifier = Modifier) {
    Text(text = text, color = MaterialTheme.colorScheme.onBackground, modifier = modifier)
}

@Composable
fun Android8CustomPermissionRefresher(state: PermissionsRequesterState) {
    val activity = LocalActivity.current
    LaunchedEffect(activity, state.customPermissions) {
        /**
         * Due to a bug in Android 8 and 8.1, Settings.canDrawOverlay() returns false even
         * after the user has granted the permission for some period of time, so we check
         * for its value each second in such cases
         *
         * https://issuetracker.google.com/issues/66072795
         */
        if (Build.VERSION.SDK_INT in listOf(Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1)) {
            val permission = state.customPermissions.first()
            when (val oldState = permission.state) {
                PermissionState.Unknown -> Unit
                PermissionState.Granted -> Unit
                is PermissionState.Declined -> tickerFlow(period = 1.seconds).onEach {
                    state.customPermissions = listOf(permission.copy(
                        state = when (val newState = permission.strategy.findState(activity)) {
                            is PermissionState.DeclinedOnce -> {
                                newState.copy(showRationale = oldState.showRationale)
                            }
                            is PermissionState.DeclinedPermanently -> {
                                newState.copy(showRationale = oldState.showRationale)
                            }
                            PermissionState.Granted -> newState
                            PermissionState.Unknown -> newState
                        }
                    ))
                }.launchIn(this)
            }
        }
    }
}
