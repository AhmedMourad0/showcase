package dev.ahmedmourad.showcase.common.screens.permissions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.ViewModel
import dev.ahmedmourad.showcase.common.initializers.appCtx


@Stable
open class PermissionsRequesterViewModel(handle: Handle) : ViewModel(handle) {
    val state = PermissionsRequesterState()
}

@Stable
class PermissionsRequesterState {
    var enabledRequester by mutableStateOf(RequesterType.None)
    var singlePermission by mutableStateOf(permissionsFor(RequesterType.Single))
    var multiplePermissions by mutableStateOf(permissionsFor(RequesterType.Multiple))
    var customPermissions by mutableStateOf(permissionsFor(RequesterType.Custom))
    var rationaleContentPermissions by mutableStateOf(permissionsFor(RequesterType.RationaleContent))
    var grantedContentPermissions by mutableStateOf(permissionsFor(RequesterType.GrantedContent))
    var onAllGrantedPermissions by mutableStateOf(permissionsFor(RequesterType.OnAllGranted))
    var rationaleFirstPermissions by mutableStateOf(permissionsFor(RequesterType.RationaleFirst))
}

fun PermissionsRequesterState.switchTo(type: RequesterType) {
    enabledRequester = type
    when (type) {
        RequesterType.None -> Unit
        RequesterType.Single -> singlePermission = permissionsFor(type)
        RequesterType.Multiple -> multiplePermissions = permissionsFor(type)
        RequesterType.Custom -> customPermissions = permissionsFor(type)
        RequesterType.RationaleContent -> rationaleContentPermissions = permissionsFor(type)
        RequesterType.GrantedContent -> grantedContentPermissions = permissionsFor(type)
        RequesterType.OnAllGranted -> onAllGrantedPermissions = permissionsFor(type)
        RequesterType.RationaleFirst -> rationaleFirstPermissions = permissionsFor(type)
    }
}

fun permissionsFor(type: RequesterType, activity: Activity? = null): List<Permission> {
    val permissions = when (type) {
        RequesterType.None -> emptyList()
        RequesterType.Single -> listOf(Permission(Manifest.permission.ACCESS_COARSE_LOCATION))
        RequesterType.Multiple -> listOf(
            Permission(Manifest.permission.RECORD_AUDIO),
            Permission(Manifest.permission.READ_CONTACTS)
        )
        RequesterType.Custom -> listOf(Permission(
            id = Manifest.permission.SYSTEM_ALERT_WINDOW,
            strategy = SystemAlertWindowPermissionStrategy
        ))
        RequesterType.RationaleContent -> listOf(Permission(Manifest.permission.BODY_SENSORS))
        RequesterType.GrantedContent -> listOf(Permission(Manifest.permission.CAMERA))
        RequesterType.OnAllGranted -> listOf(Permission(Manifest.permission.SEND_SMS))
        RequesterType.RationaleFirst -> listOf(Permission(
            id = Manifest.permission.CALL_PHONE,
            rationalePriority = RationalePriority.ShowRationaleFirst
        ))
    }
    return if (activity != null) {
        permissions.map { it.copy(state = it.strategy.findState(activity)) }
    } else {
        permissions
    }
}

@Immutable
enum class RequesterType {
    None, Single, Multiple, Custom, RationaleContent, GrantedContent, OnAllGranted, RationaleFirst
}

@Immutable
object SystemAlertWindowPermissionStrategy : PermissionStrategy {
    override fun isGranted(activity: Activity): Boolean {
        return Settings.canDrawOverlays(activity)
    }
    override fun wasDeclinedOnce(activity: Activity): Boolean = false
    override fun gotoSettings(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.fromParts("package", appCtx.packageName, null)
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        appCtx.startActivity(intent)
    }
}
