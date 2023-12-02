package dev.ahmedmourad.showcase.common.screens.permissions

import androidx.compose.runtime.Immutable

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
