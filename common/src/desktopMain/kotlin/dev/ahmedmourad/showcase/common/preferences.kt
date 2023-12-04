package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.screens.themeselector.ThemeSchemes
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

    actual suspend fun selectTheme(value: ThemeSchemes) {

    }

    actual fun selectedTheme(otherwise: () -> ThemeSchemes): Flow<ThemeSchemes> {
        return flowOf(otherwise())
    }
}
