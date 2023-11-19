package dev.ahmedmourad.showcase.common.compose.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.LayoutDirection

@Stable
fun Modifier.mirror(direction: LayoutDirection): Modifier = this.scale(
    scaleX = if (direction == LayoutDirection.Ltr) 1f else -1f,
    scaleY = 1f
)
