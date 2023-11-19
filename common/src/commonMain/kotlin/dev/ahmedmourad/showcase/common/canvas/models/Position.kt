package dev.ahmedmourad.showcase.common.canvas.models

import androidx.compose.runtime.Immutable

@Immutable
enum class Position {
    TopStart, TopCenter, TopEnd,
    MiddleStart, MiddleCenter, MiddleEnd,
    BottomStart, BottomCenter, BottomEnd
}
