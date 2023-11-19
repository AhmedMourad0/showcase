package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class Route<R : Any>(
    val key: RouteKey<R>,
    val destination: Destination<R>
)

@Composable
fun <R : Any> Route<R>.content() {
    this.destination.content(DestinationScope(Navigator(this.key, LocalNavigationManager.current)))
}
