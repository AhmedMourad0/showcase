package dev.ahmedmourad.showcase.common.canvas.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntOffset
import dev.ahmedmourad.showcase.common.canvas.models.Position

@Immutable
data class QuickActionsLayoutInfo(
    val x: Float,
    val y: Float,
    val position: Position,
    val animationOffset: IntOffset
)
