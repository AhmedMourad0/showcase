package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.ColorFilter
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.LocalThemeManager
import dev.ahmedmourad.showcase.common.compose.theme.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ThemeModeAction(themeManager: ThemeManager, scope: CoroutineScope) = ActionButton(icon = {
    if (LocalThemeManager.current.collectIsDarkAsState()) {
        Icons.Rounded.LightMode
    } else {
        Icons.Rounded.DarkMode
    }
}, colorFilter = {
    ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
}, onClick = {
    scope.launch {
        themeManager.toggleMode()
    }
})

fun GotoScheduleViewerAction(onClick: () -> Unit) = ActionButton(
    icon = { Icons.Rounded.Today },
    colorFilter = { ColorFilter.tint(MaterialTheme.colorScheme.onBackground) },
    enabled = { Showcase.acceptsInputs },
    onClick = onClick
)

fun DeleteAction(enabled: Boolean, onClick: () -> Unit) = ActionButton(
    icon = { Icons.Rounded.Delete },
    colorFilter = { ColorFilter.tint(MaterialTheme.colorScheme.error) },
    enabled = { Showcase.acceptsInputs && enabled },
    onClick = onClick
)

fun CloseAction(onClick: () -> Unit) = ActionButton(
    icon = { Icons.Rounded.Close },
    colorFilter = { ColorFilter.tint(MaterialTheme.colorScheme.onBackground) },
    enabled = { Showcase.acceptsInputs },
    onClick = onClick
)

fun ConfirmAction(enabled: Boolean, onClick: () -> Unit) = ActionButton(
    icon = { Icons.Rounded.Check },
    colorFilter = { ColorFilter.tint(MaterialTheme.colorScheme.onBackground) },
    enabled = { Showcase.acceptsInputs && enabled },
    onClick = onClick
)
