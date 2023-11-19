package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import dev.ahmedmourad.showcase.common.randomUUID

@Immutable
data class BackHandler(
    val id: String,
    val enabled: Boolean,
    val onBack: () -> Unit
)

@Composable
fun <R : Any> DestinationScope<R>.BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val id = remember { randomUUID() }
    DisposableEffect(this, id, enabled, onBack) {
        navigator.register(BackHandler(
            id = id,
            enabled = enabled,
            onBack = onBack
        ))
        onDispose {
            navigator.unregister(id)
        }
    }
}
