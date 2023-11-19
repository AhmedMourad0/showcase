package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Stable

@Stable
class Navigator<R : Any>(
    val routeKey: RouteKey<R>,
    val navManager: NavigationManager
) {

    suspend fun pop(result: R?) {
        navManager.pop(result)
    }

    suspend fun <R1 : Any> push(key: String, destination: Destination<R1>, onResult: (R1?) -> Unit) {
        navManager.push(
            key = key,
            destination = destination,
            onResult = onResult
        )
    }

    fun register(backHandler: BackHandler): BackHandler {
        return navManager.register(routeKey, backHandler)
    }

    fun unregister(id: String) {
        navManager.unregister(routeKey, id)
    }
}

fun <R : Any> Navigator<R>.update(transform: (Destination<R>) -> Destination<R>) {
    this.navManager.update(
        routeKey = routeKey,
        transform = transform
    )
}

suspend fun Navigator<Unit>.pop() = this.pop(Unit)

suspend fun Navigator<*>.push(key: String, destination: Destination<Unit>) = this.push(
    key = key,
    destination = destination,
    onResult = { }
)
