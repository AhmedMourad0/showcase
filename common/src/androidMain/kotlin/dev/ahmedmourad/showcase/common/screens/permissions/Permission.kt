package dev.ahmedmourad.showcase.common.screens.permissions

import androidx.compose.runtime.Immutable

@Immutable
enum class RationalePriority {
    ShowRationaleFirst, RequestPermissionFirst
}

@Immutable
data class Permission(
    val id: String,
    val state: PermissionState = PermissionState.Unknown,
    val rationalePriority: RationalePriority = RationalePriority.RequestPermissionFirst,
    val strategy: PermissionStrategy = DefaultPermissionStrategy(id)
)
