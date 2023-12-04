package dev.ahmedmourad.showcase.common.compose.theme

import androidx.compose.runtime.*
import dev.ahmedmourad.showcase.common.PreferenceManager
import dev.ahmedmourad.showcase.common.defaultIsInDarkMode
import dev.ahmedmourad.showcase.common.defaultSelectedTheme
import dev.ahmedmourad.showcase.common.screens.themeselector.ThemeSchemes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Stable
class ThemeManager(
    private val prefManager: PreferenceManager = PreferenceManager()
) {

    @Composable
    fun collectIsDarkAsState(): Boolean {
        return prefManager
            .isInDarkMode(::defaultIsInDarkMode)
            .collectAsState(runBlocking { prefManager.isInDarkMode(::defaultIsInDarkMode).first() })
            .value
    }

    suspend fun toggleMode() {
        prefManager.toggleIsInDarkMode()
    }

    @Composable
    fun collectSelectedThemeState(): ThemeSchemes {
        return prefManager
            .selectedTheme(::defaultSelectedTheme)
            .collectAsState(runBlocking { prefManager.selectedTheme(::defaultSelectedTheme).first() })
            .value
    }

    suspend fun selectTheme(schemes: ThemeSchemes) {
        prefManager.selectTheme(schemes)
    }
}

val LocalThemeManager = compositionLocalOf<ThemeManager> {
    error("CompositionLocal LocalThemeManager not present")
}
