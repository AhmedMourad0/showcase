package dev.ahmedmourad.showcase.common.screens.permissions

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import dev.ahmedmourad.showcase.common.utils.LifecycleEffect
import dev.ahmedmourad.showcase.common.utils.LocalActivity

@Composable
fun PermissionRequester(
    permissions: () -> List<Permission>,
    onPermissionsChange: (List<Permission>) -> Unit,
    onAllGranted: () -> Unit = { },
    enabled: () -> Boolean = { true },
    rationaleContent: @Composable RationaleContentScope.() -> Unit,
    grantedContent: @Composable () -> Unit = { },
) {
    val activity = LocalActivity.current
    val rationaleFirstScope = remember(permissions(), onPermissionsChange, activity) {
        createRationaleFirstContentScope(
            permissions = permissions(),
            onPermissionsChange = onPermissionsChange,
            activity = activity
        )
    }
    val declinedPermissionsScope = remember(permissions(), onPermissionsChange, activity) {
        createDeclinedPermissionsRationaleContentScope(
            permissions = permissions(),
            onPermissionsChange = onPermissionsChange,
            activity = activity
        )
    }
    if (enabled()) {
        if (rationaleFirstScope != null) {
            rationaleContent(rationaleFirstScope)
        } else if (declinedPermissionsScope != null) {
            rationaleContent(declinedPermissionsScope)
        }
    }
    PermissionsSynchronizer(
        permissions = permissions,
        onPermissionsChange = onPermissionsChange
    )
    PermissionsLauncher(
        permissions = permissions,
        onPermissionsChange = onPermissionsChange,
        enabled = enabled,
        hasRationaleFirstPermissions = rationaleFirstScope != null
    )
    GrantedContent(
        permissions = permissions,
        onAllGranted = onAllGranted,
        content = grantedContent
    )
}

@Composable
private fun PermissionsSynchronizer(
    permissions: () -> List<Permission>,
    onPermissionsChange: (List<Permission>) -> Unit
) {
    val activity = LocalActivity.current
    LifecycleEffect(Lifecycle.Event.ON_START, skip = 0) {
        //to re-sync permissions after the user returns to the app
        onPermissionsChange(permissions().map { it.copy(state = it.strategy.findState(activity)) })
    }
}

@Composable
private fun GrantedContent(
    permissions: () -> List<Permission>,
    onAllGranted: () -> Unit,
    content: @Composable () -> Unit
) {
    val areAllGranted = remember(permissions()) {
        permissions().all { it.state == PermissionState.Granted }
    }
    LaunchedEffect(areAllGranted) {
        if (areAllGranted) {
            onAllGranted.invoke()
        }
    }
    if (areAllGranted) {
        content.invoke()
    }
}

@Composable
private fun PermissionsLauncher(
    permissions: () -> List<Permission>,
    onPermissionsChange: (List<Permission>) -> Unit,
    enabled: () -> Boolean,
    hasRationaleFirstPermissions: Boolean
) {
    val activity = LocalActivity.current
    val unknownPermissions = remember(permissions()) {
        permissions().filter { it.state == PermissionState.Unknown }
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        onPermissionsChange(permissions().map { permission ->
            permission.copy(state = permission.strategy.findState(activity))
        })
    }
    LaunchedEffect(unknownPermissions, enabled()) {
        //we check for unknown permissions as soon as they pop up
        unknownPermissions
            .takeIf { enabled() && !hasRationaleFirstPermissions }
            ?.map { it.id }
            ?.toTypedArray()
            ?.takeIf { it.isNotEmpty() }
            ?.let(launcher::launch)
    }
}

private fun createRationaleFirstContentScope(
    permissions: List<Permission>,
    onPermissionsChange: (List<Permission>) -> Unit,
    activity: Activity
): RationaleContentScope? {
    val rationaleFirstPermissions = findRationaleFirstPermissions(permissions, activity)
    if (rationaleFirstPermissions.isEmpty()) return null
    return RationaleContentScope(
        permissions = rationaleFirstPermissions,
        reason = RationaleReason.DeclinedOnce,
        onDismiss = {
            onPermissionsChange(permissions.map { it.copy(rationalePriority = RationalePriority.RequestPermissionFirst) })
        }, onAction = {
            onDismiss()
        }
    )
}

private fun createDeclinedPermissionsRationaleContentScope(
    permissions: List<Permission>,
    onPermissionsChange: (List<Permission>) -> Unit,
    activity: Activity
): RationaleContentScope? {
    val declinedPermissions = findDeclinedPermissions(permissions)
    if (declinedPermissions.isEmpty()) return null
    //we show a dialog for declined-once permissions first,
    // once that's handled we show the dialog for declined-permanently permissions
    val rationaleReason = if (declinedPermissions.any { it.state is PermissionState.DeclinedOnce }) {
        RationaleReason.DeclinedOnce
    } else {
        RationaleReason.DeclinedPermanently
    }
    val dialogPermissions = declinedPermissions.filter {
        when (rationaleReason) {
            RationaleReason.DeclinedOnce -> it.state is PermissionState.DeclinedOnce
            RationaleReason.DeclinedPermanently -> it.state is PermissionState.DeclinedPermanently
        }
    }
    return RationaleContentScope(
        permissions = dialogPermissions,
        reason = rationaleReason,
        onDismiss = {
            onPermissionsChange(permissions.map { permission ->
                if (permission in dialogPermissions) {
                    permission.copy(state = when (val state = permission.state) {
                        is PermissionState.DeclinedOnce -> state.copy(showRationale = false)
                        is PermissionState.DeclinedPermanently -> state.copy(showRationale = false)
                        PermissionState.Granted -> state
                        PermissionState.Unknown -> state
                    }, rationalePriority = RationalePriority.RequestPermissionFirst)
                } else {
                    permission
                }
            })
        }, onAction = {
            onDismiss()
            when (rationaleReason) {
                RationaleReason.DeclinedOnce -> {
                    onPermissionsChange(permissions.map { permission ->
                        if (permission in dialogPermissions) {
                            permission.copy(
                                state = PermissionState.Unknown,
                                rationalePriority = RationalePriority.RequestPermissionFirst
                            )
                        } else {
                            permission
                        }
                    })
                }
                RationaleReason.DeclinedPermanently -> {
                    dialogPermissions.first().strategy.gotoSettings(activity)
                }
            }
        }
    )
}

private fun findRationaleFirstPermissions(
    permissions: List<Permission>,
    activity: Activity
): List<Permission> {
    return permissions.filter {
        it.rationalePriority == RationalePriority.ShowRationaleFirst &&
                it.state == PermissionState.Unknown
    }.map {
        it.copy(state = it.strategy.findState(activity))
    }.filter { it.state != PermissionState.Granted }
}

private fun findDeclinedPermissions(permissions: List<Permission>): List<Permission> {
    // we don't want to show our dialog below the system permission dialog
    if (permissions.any { it.state == PermissionState.Unknown }) {
        return emptyList()
    }
    return permissions.filter { permission ->
        when (val state = permission.state) {
            PermissionState.Granted -> false
            PermissionState.Unknown -> false
            is PermissionState.Declined -> state.showRationale
        }
    }
}
