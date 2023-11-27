package dev.ahmedmourad.showcase.common.screens.canvas.undo

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import dev.ahmedmourad.showcase.common.screens.canvas.models.CanvasItem

@Immutable
sealed interface CanvasAction {

    @Immutable
    sealed interface TransformativeAction : CanvasAction {
        val canvasId: String
    }

    @Immutable
    data class Add(val item: CanvasItem) : CanvasAction
    @Immutable
    data class Remove(val canvasId: String) : CanvasAction

    @Immutable
    data class Transform(
        override val canvasId: String,
        val offset: Offset,
        val z: Int,
        val scaleX: Float,
        val scaleY: Float,
        val rotationZ: Float
    ) : TransformativeAction {
        constructor(item: CanvasItem) : this(
            canvasId = item.canvasId,
            offset = item.offset,
            z = item.z,
            scaleX = item.scaleX,
            scaleY = item.scaleY,
            rotationZ = item.rotationZ
        )
    }

    @Immutable
    data class UpdateZ(
        override val canvasId: String,
        val z: Int
    ) : TransformativeAction

    @Immutable
    data class Flip(override val canvasId: String) : TransformativeAction
}
