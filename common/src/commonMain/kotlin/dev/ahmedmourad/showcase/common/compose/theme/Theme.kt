package dev.ahmedmourad.showcase.common.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import dev.ahmedmourad.showcase.common.compose.theme.schemes.TestDarkScheme
import dev.ahmedmourad.showcase.common.compose.theme.schemes.TestLightScheme

const val DisabledContainerOpacity = 0.12f
const val DisabledTextOpacity = 0.38f

@Composable
fun ShowcaseTheme(
    darkTheme: Boolean = true,
    provideColorScheme: (isDark: Boolean) -> ColorScheme? = { null },
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalThemeManager provides ThemeManager(darkTheme)) {
        val isDark = LocalThemeManager.current.collectIsDarkAsState()
        val colorScheme = if (isDark) {
            provideColorScheme(true) ?: TestDarkScheme
        } else {
            provideColorScheme(false) ?: TestLightScheme
        }
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
