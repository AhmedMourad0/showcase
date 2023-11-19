package dev.ahmedmourad.showcase.common.ios

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import dev.ahmedmourad.showcase.common.randomUUID

@Immutable
enum class UIControllerEvent {
    ViewWillDisappear, OnDestroy
}

@Immutable
data class UIControllerObserver(
    val event: UIControllerEvent,
    val onEvent: () -> Unit
)

@Stable
interface ObservableUIController {
    fun addObserver(id: String, observer: UIControllerObserver)
    fun removeObserver(id: String)
}

val LocalObservableUIController = staticCompositionLocalOf<ObservableUIController> {
    error("CompositionLocal ObservableUIController not present")
}

@Composable
fun UIControllerLifecycleEffect(
    event: UIControllerEvent,
    onEvent: () -> Unit
) {
    val controller = LocalObservableUIController.current
    val id = remember { randomUUID() }
    DisposableEffect(controller, id, onEvent) {
        controller.addObserver(
            id = id,
            observer = UIControllerObserver(
                event = event,
                onEvent = onEvent
            )
        )
        onDispose {
            controller.removeObserver(id)
        }
    }
}
