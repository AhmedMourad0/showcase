package dev.ahmedmourad.showcase.common.screens.themeselector.schemes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import dev.ahmedmourad.showcase.common.screens.themeselector.ThemeSchemes

val OrangeBrownLightScheme = lightColorScheme(
    primary = Color(0xFF805600),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDDB0),
    onPrimaryContainer = Color(0xFF281800),
    secondary = Color(0xFF6F5B40),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF9DEBB),
    onSecondaryContainer = Color(0xFF261904),
    tertiary = Color(0xFF506441),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD2EABD),
    onTertiaryContainer = Color(0xFF0F2004),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1F1B16),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1F1B16),
    surfaceVariant = Color(0xFFEFE0CF),
    onSurfaceVariant = Color(0xFF4F4539),
    outline = Color(0xFF817567),
    inverseOnSurface = Color(0xFFF9EFE7),
    inverseSurface = Color(0xFF34302A),
    inversePrimary = Color(0xFFFFBA46),
    surfaceTint = Color(0xFF805600),
    outlineVariant = Color(0xFFD2C4B4),
    scrim = Color(0xFF000000)
)

val OrangeBrownDarkScheme = darkColorScheme(
    primary = Color(0xFFFFBA46),
    onPrimary = Color(0xFF442C00),
    primaryContainer = Color(0xFF614000),
    onPrimaryContainer = Color(0xFFFFDDB0),
    secondary = Color(0xFFDCC3A1),
    onSecondary = Color(0xFF3D2E16),
    secondaryContainer = Color(0xFF55442A),
    onSecondaryContainer = Color(0xFFF9DEBB),
    tertiary = Color(0xFFB7CEA2),
    onTertiary = Color(0xFF233517),
    tertiaryContainer = Color(0xFF394C2B),
    onTertiaryContainer = Color(0xFFD2EABD),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1F1B16),
    onBackground = Color(0xFFEAE1D9),
    surface = Color(0xFF1F1B16),
    onSurface = Color(0xFFEAE1D9),
    surfaceVariant = Color(0xFF4F4539),
    onSurfaceVariant = Color(0xFFD2C4B4),
    outline = Color(0xFF9B8F80),
    inverseOnSurface = Color(0xFF1F1B16),
    inverseSurface = Color(0xFFEAE1D9),
    inversePrimary = Color(0xFF805600),
    surfaceTint = Color(0xFFFFBA46),
    outlineVariant = Color(0xFF4F4539),
    scrim = Color(0xFF000000)
)

val OrangeBrownSchemes = ThemeSchemes(
    id = "orange_brown",
    name = "Orange Brown",
    light = OrangeBrownLightScheme,
    dark = OrangeBrownDarkScheme
)

//Seed = Color(0xFFF3AD33)
