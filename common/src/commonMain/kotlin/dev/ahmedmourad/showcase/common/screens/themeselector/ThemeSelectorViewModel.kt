package dev.ahmedmourad.showcase.common.screens.themeselector

import androidx.compose.runtime.Stable
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.ViewModel

@Stable
open class ThemeSelectorViewModel(
    handle: Handle,
    schemes: List<ThemeSchemes> = CommonSchemes
) : ViewModel(handle) {
    val state = ThemeSelectorState(schemes)
}

@Stable
class ThemeSelectorState(@Stable val schemes: List<ThemeSchemes>) {

}
