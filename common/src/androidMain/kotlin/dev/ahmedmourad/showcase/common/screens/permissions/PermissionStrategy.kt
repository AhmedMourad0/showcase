package dev.ahmedmourad.showcase.common.screens.permissions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Immutable
import androidx.core.content.ContextCompat
import dev.ahmedmourad.showcase.common.initializers.appCtx

@Immutable
interface PermissionStrategy {
    fun isGranted(activity: Activity): Boolean
    fun wasDeclinedOnce(activity: Activity): Boolean
    fun gotoSettings(activity: Activity)
}

fun PermissionStrategy.findState(activity: Activity): PermissionState {
    return when {
        isGranted(activity) -> PermissionState.Granted
        wasDeclinedOnce(activity) -> PermissionState.DeclinedOnce(true)
        else -> PermissionState.DeclinedPermanently(true)
    }
}

@Immutable
data class DefaultPermissionStrategy(private val id: String) : PermissionStrategy {
    override fun isGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(appCtx, this.id) == PackageManager.PERMISSION_GRANTED
    }
    override fun wasDeclinedOnce(activity: Activity): Boolean {
        return activity.shouldShowRequestPermissionRationale(this.id)
    }
    override fun gotoSettings(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", appCtx.packageName, null)
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        appCtx.startActivity(intent)
    }
}
