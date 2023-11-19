package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class ActionButton(
    val icon: @Composable () -> ImageVector,
    val colorFilter: @Composable () -> ColorFilter,
    val enabled: @Composable () -> Boolean = { true },
    val onClick: () -> Unit = { },
)

@Composable
fun <R : Any> DestinationScope<R>.NavigationBarActions(
    startActions: () -> List<ActionButton> = { emptyList() },
    endActions: () -> List<ActionButton> = { emptyList() }
) {
    val navManager = LocalNavigationManager.current
    DisposableEffect(navManager, startActions, endActions) {
        navigator.update {
            it.copy(barActions = BarActions(
                start = startActions(),
                end = endActions()
            ))
        }
        onDispose {
            navigator.update {
                it.copy(barActions = BarActions(
                    start = emptyList(),
                    end = emptyList()
                ))
            }
        }
    }
}
