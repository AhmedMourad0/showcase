package dev.ahmedmourad.showcase.common.canvas.models

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import dev.ahmedmourad.showcase.common.randomUUID

@Stable
class CanvasItem(
    imageUrl: String,
    width: Dp,
    height: Dp,
    offset: Offset,
    z: Int,
    rotation: Float,
    scaleX: Float,
    scaleY: Float,
    flip: Boolean,
) {
    var imageUrl by mutableStateOf(imageUrl)
    var width by mutableStateOf(width)
    var height by mutableStateOf(height)
    var offset by mutableStateOf(offset)
    var z by mutableStateOf(z)
    var rotationZ by mutableStateOf(rotation)
    var scaleX by mutableStateOf(scaleX)
    var scaleY by mutableStateOf(scaleY)
    var flip by mutableStateOf(flip)
    val canvasId: String = randomUUID()
}

val CanvasItem.x get() = this.offset.x
val CanvasItem.y get() = this.offset.y
