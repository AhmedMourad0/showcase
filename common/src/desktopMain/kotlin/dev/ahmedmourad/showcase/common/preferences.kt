package dev.ahmedmourad.showcase.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class PreferenceManager {
    actual suspend fun toggleIsInDarkMode() {

    }
    actual suspend fun setIsInDarkMode(value: Boolean) {

    }
    actual fun isInDarkMode(otherwise: () -> Boolean): Flow<Boolean> {
        return flowOf(otherwise())
    }
}
