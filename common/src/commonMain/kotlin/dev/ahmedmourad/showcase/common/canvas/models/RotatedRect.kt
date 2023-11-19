package dev.ahmedmourad.showcase.common.canvas.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

/**
 * Similar to [androidx.compose.ui.geometry.Rect] except that it accounts for the rect being at an angle
 */
@Immutable
data class RotatedRect(
    val topLeft: Offset,
    val topRight: Offset,
    val bottomLeft: Offset,
    val bottomRight: Offset,
    val center: Offset
) {
    val start by lazy { minOf(topLeft.x, topRight.x, bottomLeft.x, bottomRight.x) }
    val end by lazy { maxOf(topLeft.x, topRight.x, bottomLeft.x, bottomRight.x) }
    val top by lazy { minOf(topLeft.y, topRight.y, bottomLeft.y, bottomRight.y) }
    val bottom by lazy { maxOf(topLeft.y, topRight.y, bottomLeft.y, bottomRight.y) }
}

fun RotatedRect.containsInside(offset: Offset): Boolean {
    return offset.x in this.start..this.end && offset.y in this.top..this.bottom
}
