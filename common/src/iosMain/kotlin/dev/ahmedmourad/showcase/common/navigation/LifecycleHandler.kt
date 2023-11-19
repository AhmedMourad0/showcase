package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import dev.ahmedmourad.showcase.common.randomUUID

@Immutable
enum class LifecycleEvent {
    OnStop
}

@Immutable
data class LifecycleHandler(
    val id: String,
    val event: LifecycleEvent,
    val onEvent: () -> Unit
)

@Composable
fun <R : Any> DestinationScope<R>.LifecycleEffect(
    event: LifecycleEvent,
    onEvent: () -> Unit
) {
    val id = remember { randomUUID() }
    DisposableEffect(this, id, event, onEvent) {
        navigator.update {
            it.copy(lifecycleHandlers = it.lifecycleHandlers + LifecycleHandler(
                id = id,
                event = event,
                onEvent = onEvent
            ))
        }
        onDispose {
            navigator.update { dest ->
                dest.copy(lifecycleHandlers = dest.lifecycleHandlers.filter { it.id != id })
            }
        }
    }
}
