package dev.ahmedmourad.showcase.common.compose.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

const val DisabledContainerOpacity = 0.12f
const val DisabledTextOpacity = 0.38f

@Composable
fun ShowcaseTheme(content: @Composable () -> Unit, ) {
    CompositionLocalProvider(LocalThemeManager provides ThemeManager()) {
        val themeManager = LocalThemeManager.current
        val isDark = themeManager.collectIsDarkAsState()
        val schemes = themeManager.collectSelectedThemeState()
        val colorScheme = if (isDark) schemes.dark else schemes.light
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = {
                CompositionLocalProvider(LocalContentColor provides colorScheme.primary) {
                    ProvideTextStyle(value = TextStyle(
                        color = colorScheme.onBackground,
                        fontFamily = FontFamily.SansSerif
                    ), content = content)
                }
            }
        )
    }
}
