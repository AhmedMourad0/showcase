package dev.ahmedmourad.showcase.common.compose.theme

import androidx.compose.runtime.*
import dev.ahmedmourad.showcase.common.PreferenceManager
import dev.ahmedmourad.showcase.common.defaultIsInDarkMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Stable
class ThemeManager(
    isSystemInDarkTheme: Boolean,
    private val prefManager: PreferenceManager = PreferenceManager()
) {

    @Composable
    fun collectIsDarkAsState(): Boolean {
        val isDark by prefManager
            .isInDarkMode(::defaultIsInDarkMode)
            .collectAsState(runBlocking { prefManager.isInDarkMode(::defaultIsInDarkMode).first() })
        return isDark
    }

    suspend fun toggleMode() {
        prefManager.toggleIsInDarkMode()
    }
}

val LocalThemeManager = compositionLocalOf<ThemeManager> {
    error("CompositionLocal LocalThemeManager not present")
}
