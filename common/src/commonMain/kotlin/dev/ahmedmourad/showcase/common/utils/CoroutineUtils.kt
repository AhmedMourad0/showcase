package dev.ahmedmourad.showcase.common.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

fun tickerFlow(period: Duration, initialDelay: Duration = period) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}
