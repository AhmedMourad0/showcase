package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.blocker
import kotlin.experimental.ExperimentalNativeApi

val LocalNavigationManager = staticCompositionLocalOf<NavigationManager> {
    error("CompositionLocal NavigationManager not present")
}

@Stable
class NavigationManager(
    key: String = "Root",
    destination: Destination<Unit>
) {

    val backstack = mutableStateListOf<Route<*>>(Route(RouteKey(key), destination))
    val topScreenOffsetRatio = Animatable(0f)
    var handleDragDirection by mutableStateOf(DragDirection.Start)

    suspend fun onBack() = Showcase.blocker {
        if (!triggerBackHandlers()) {
            pop(null)
        }
    }

    fun triggerBackHandlers(): Boolean {
        return currentScreen?.destination?.backHandlers
            ?.filter { it.enabled }
            .orEmpty()
            .onEach { it.onBack() }
            .isNotEmpty()
    }

    suspend fun <R : Any> pop(result: R?) = Showcase.blocker {
        topScreenOffsetRatio.animateTo(1f)
        backstack.removeLast().also {
            it.onStop()
            it.destination.handleResult(result)
        }
        topScreenOffsetRatio.snapTo(0f)
    }

    suspend fun <R : Any> push(
        key: String,
        destination: Destination<R>,
        onResult: (R?) -> Unit
    ) = Showcase.blocker {
        backstack.add(Route(RouteKey(key), destination.copy(resultHandler = onResult)))
        prevScreen?.onStop()
        topScreenOffsetRatio.snapTo(1f)
        topScreenOffsetRatio.animateTo(0f)
    }

    fun <R : Any> register(key: RouteKey<R>, backHandler: BackHandler): BackHandler {
        update(key) { destination ->
            destination.copy(
                backHandlers = destination.backHandlers.filter { it.id != backHandler.id } + backHandler
            )
        }
        return backHandler
    }

    fun <R : Any> unregister(key: RouteKey<R>, id: String) {
        update(key) { destination ->
            destination.copy(backHandlers = destination.backHandlers.filter { it.id != id })
        }
    }
}

fun Route<*>.onStop() {
    this.destination.lifecycleHandlers.forEach {
        when (it.event) {
            LifecycleEvent.OnStop -> it.onEvent()
        }
    }
}

val NavigationManager.currentScreen: Route<*>? get() = backstack.lastOrNull()
val NavigationManager.prevScreen: Route<*>? get() = backstack.getOrNull(backstack.lastIndex - 1)

@OptIn(ExperimentalNativeApi::class)
fun <R : Any> NavigationManager.update(routeKey: RouteKey<R>, transform: (Destination<R>) -> Destination<R>) {
    backstack.replaceAll {
        if (it.key == routeKey) {
            @Suppress("UNCHECKED_CAST")
            Route(
                key = routeKey,
                destination = transform(it.destination as Destination<R>)
            )
        } else {
            it
        }
    }
}
