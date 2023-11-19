package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlin.reflect.KClass

@Immutable
data class BarActions(
    val start: List<ActionButton> = emptyList(),
    val end: List<ActionButton> =  emptyList()
)

@Immutable
data class Destination<R : Any>(
    val title: String?,
    val barActions: BarActions = BarActions(),
    val lifecycleHandlers: List<LifecycleHandler> = emptyList(),
    val backHandlers: List<BackHandler> = emptyList(),
    val resultHandler: (R?) -> Unit = { },
    val content: @Composable DestinationScope<R>.() -> Unit
)

fun <R : Any> Destination<*>.handleResult(result: R?) {
    @Suppress("UNCHECKED_CAST")
    (this as Destination<R>).resultHandler(result)
}
