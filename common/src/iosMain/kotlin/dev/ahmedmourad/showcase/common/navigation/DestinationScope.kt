package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Immutable

@Immutable
value class DestinationScope<R : Any>(val navigator: Navigator<R>)

@Immutable
value class RouteKey<R : Any>(val v: String)
