package dev.ahmedmourad.showcase.common.screens.themeselector

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.ahmedmourad.showcase.common.screens.themeselector.schemes.*

@Immutable
class ThemeSchemes(
    val id: String,
    val name: String,
    val light: ColorScheme,
    val dark: ColorScheme
)

@Stable
val CommonSchemes: List<ThemeSchemes> = buildList { 
    add(DarkBlueSchemes)
    add(OrangeBrownSchemes)
}
