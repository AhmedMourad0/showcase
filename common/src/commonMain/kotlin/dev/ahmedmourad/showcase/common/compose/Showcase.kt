package dev.ahmedmourad.showcase.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.ahmedmourad.showcase.common.randomUUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmInline

@JvmInline
@Immutable
value class InputBlocker(val id: String = randomUUID())

@Stable
object Showcase {
    var inputBlockers = MutableStateFlow(emptyList<InputBlocker>())
    val acceptsInputs @Composable get() = inputBlockers.collectAsState().value.isEmpty()
}

@Composable
fun Showcase.acceptsInputs(excluding: Set<InputBlocker>): Boolean {
    val blockers by inputBlockers.collectAsState()
    return blockers.minus(excluding).isEmpty()
}

fun Showcase.block(): InputBlocker {
    return InputBlocker().also { blocker -> inputBlockers.update { it + blocker } }
}

fun Showcase.unblock(blocker: InputBlocker) {
    inputBlockers.update { it.toMutableList().apply { remove(blocker) } }
}

suspend inline fun Showcase.blocker(crossinline exec: suspend () -> Unit) {
    val blocker = block()
    try {
        exec()
    } finally {
        unblock(blocker)
    }
}

fun CoroutineScope.launchBlocker(
    context: CoroutineContext = EmptyCoroutineContext + Dispatchers.Default,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start) {
    Showcase.blocker {
        block(this)
    }
}
