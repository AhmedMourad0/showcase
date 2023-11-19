package dev.ahmedmourad.showcase.common.canvas.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import dev.ahmedmourad.showcase.common.canvas.models.CanvasItem
import dev.ahmedmourad.showcase.common.canvas.models.Position
import dev.ahmedmourad.showcase.common.canvas.models.QuickActionsLayoutInfo
import dev.ahmedmourad.showcase.common.canvas.models.RotatedRect
import dev.ahmedmourad.showcase.common.canvas.models.Side
import dev.ahmedmourad.showcase.common.canvas.models.x
import dev.ahmedmourad.showcase.common.canvas.models.y
import dev.ahmedmourad.showcase.common.pickers.toPx
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

fun Offset.rotateBy(angle: Float): Offset {
    val angleInRadians = angle * PI / 180
    return Offset(
        (x * cos(angleInRadians) - y * sin(angleInRadians)).toFloat(),
        (x * sin(angleInRadians) + y * cos(angleInRadians)).toFloat()
    )
}

operator fun Offset.times(other: Offset) = Offset(
    x = this.x * other.x,
    y = this.y * other.y
)

fun Offset.rotateAround(center: Offset, angle: Float): Offset {
    //pretend the center is (0, 0)
    val localOffset = this - center
    //rotate around (0, 0)
    val rotatedLocalX = localOffset.rotateBy(angle)
    //stop pretending
    return rotatedLocalX + center
}

fun CanvasItem.toRect(density: Density): RotatedRect {

    //top left corner while respecting scale and disrespecting rotation
    val topLeftNoRotation = Offset(
        x = this.x + (this.width.div(2) - this.width.times(scaleX).div(2)).toPx(density),
        y = this.y + (this.height.div(2) - this.height.times(scaleY).div(2)).toPx(density)
    )

    //the center remains the same regardless of rotation, because we rotate around it
    val center = Offset(
        x = topLeftNoRotation.x + this.width.toPx(density).times(scaleX).div(2),
        y = topLeftNoRotation.y + this.height.toPx(density).times(scaleY).div(2)
    )

    //we start respecting the rotation
    val topLeft = topLeftNoRotation.rotateAround(center, rotationZ)

    val topRight = Offset(
        x = topLeftNoRotation.x + this.width.times(scaleX).toPx(density),
        y = topLeftNoRotation.y
    ).rotateAround(center, rotationZ)

    val bottomLeft = Offset(
        x = topLeftNoRotation.x,
        y = topLeftNoRotation.y + this.height.times(scaleY).toPx(density)
    ).rotateAround(center, rotationZ)

    val bottomRight = Offset(
        x = topLeftNoRotation.x + this.width.times(scaleX).toPx(density),
        y = topLeftNoRotation.y + this.height.times(scaleY).toPx(density)
    ).rotateAround(center, rotationZ)

    return RotatedRect(
        topLeft = topLeft,
        topRight = topRight,
        bottomLeft = bottomLeft,
        bottomRight = bottomRight,
        center = center
    )
}

fun findQuickActionsLayoutInfo(
    allowedSpace: Rect,
    itemRect: RotatedRect,
    neededSpace: Size,
    spacing: Float
): QuickActionsLayoutInfo {
    val illegalSides = Side.entries.filterNot {
        it.isAvailable(allowedSpace, itemRect, neededSpace)
    }.toSet()
    val availableSides = Side.entries.minus(illegalSides)
    return when {
        illegalSides.isEmpty() -> Position.TopCenter
        illegalSides.size == 1 -> {
            when (illegalSides.first()) {
                Side.Top -> Position.BottomCenter
                Side.Bottom -> Position.TopCenter
                Side.Start -> Position.MiddleEnd
                Side.End -> Position.MiddleStart
            }
        }
        illegalSides.size == 2 -> {
            if (availableSides.contains(Side.Top)) {
                when {
                    availableSides.contains(Side.Start) -> Position.TopStart
                    availableSides.contains(Side.End) -> Position.TopEnd
                    else -> Position.TopCenter
                }
            } else if (availableSides.contains(Side.Bottom)) {
                when {
                    availableSides.contains(Side.Start) -> Position.BottomStart
                    availableSides.contains(Side.End) -> Position.BottomEnd
                    else -> Position.BottomCenter
                }
            } else {
                Position.MiddleCenter
            }
        }
        illegalSides.size == 3 -> {
            when (availableSides.first()) {
                Side.Top -> Position.TopCenter
                Side.Bottom -> Position.BottomCenter
                Side.Start -> Position.MiddleStart
                Side.End -> Position.MiddleEnd
            }
        }
        else -> Position.MiddleCenter
    }.toQuickActionsLayoutInfo(itemRect, neededSpace, spacing)
}

private fun Side.isAvailable(
    allowedSpace: Rect,
    itemRect: RotatedRect,
    neededSpace: Size
): Boolean {
    return when (this) {
        Side.Top -> allowedSpace.top + neededSpace.height <= itemRect.top
        Side.Bottom -> itemRect.bottom + neededSpace.height <= allowedSpace.bottom
        Side.Start -> allowedSpace.left + neededSpace.height <= itemRect.start
        Side.End -> itemRect.end + neededSpace.height <= allowedSpace.right
    }
}

private fun Position.toQuickActionsLayoutInfo(
    itemRect: RotatedRect,
    neededSpace: Size,
    spacing: Float
): QuickActionsLayoutInfo {
    val hidingOffset = IntOffset(
        x = neededSpace.width.times(1.5f).roundToInt(),
        y = neededSpace.height.times(1.5f).roundToInt()
    )
    return when (this) {
        Position.TopStart -> {
            QuickActionsLayoutInfo(
                x = itemRect.start - neededSpace.width,
                y = itemRect.top - neededSpace.height,
                position = this,
                animationOffset = IntOffset(
                    x = hidingOffset.x,
                    y = hidingOffset.y
                )
            )
        }
        Position.TopCenter -> {
            QuickActionsLayoutInfo(
                x = itemRect.center.x - neededSpace.width.div(2),
                y = itemRect.top - neededSpace.height,
                position = this,
                animationOffset = IntOffset(
                    x = 0,
                    y = hidingOffset.y
                )
            )
        }
        Position.TopEnd -> {
            QuickActionsLayoutInfo(
                x = itemRect.end + spacing,
                y = itemRect.top - neededSpace.height,
                position = this,
                animationOffset = IntOffset(
                    x = hidingOffset.x.unaryMinus(),
                    y = hidingOffset.y
                )
            )
        }
        Position.MiddleStart -> {
            QuickActionsLayoutInfo(
                x = itemRect.start - neededSpace.width - spacing,
                y = itemRect.center.y - neededSpace.height.div(2),
                position = this,
                animationOffset = IntOffset(
                    x = hidingOffset.x,
                    y = 0
                )
            )
        }
        Position.MiddleCenter -> {
            QuickActionsLayoutInfo(
                x = itemRect.center.x - neededSpace.width.div(2),
                y = itemRect.center.y - neededSpace.height.div(2),
                position = this,
                animationOffset = IntOffset(
                    x = 0,
                    y = 0
                )
            )
        }
        Position.MiddleEnd -> {
            QuickActionsLayoutInfo(
                x = itemRect.end + spacing,
                y = itemRect.center.y - neededSpace.height.div(2),
                position = this,
                animationOffset = IntOffset(
                    x = hidingOffset.x.unaryMinus(),
                    y = 0
                )
            )
        }
        Position.BottomStart -> {
            QuickActionsLayoutInfo(
                x = itemRect.start - neededSpace.width,
                y = itemRect.bottom + spacing,
                position = this,
                animationOffset = IntOffset(
                    x = hidingOffset.x,
                    y = hidingOffset.y.unaryMinus()
                )
            )
        }
        Position.BottomCenter -> {
            QuickActionsLayoutInfo(
                x = itemRect.center.x - neededSpace.width.div(2),
                y = itemRect.bottom + spacing,
                position = this,
                animationOffset = IntOffset(
                    x = 0,
                    y = hidingOffset.y.unaryMinus()
                )
            )
        }
        Position.BottomEnd -> {
            QuickActionsLayoutInfo(
                x = itemRect.end,
                y = itemRect.bottom + spacing,
                position = this,
                animationOffset = IntOffset(
                    x = hidingOffset.x.unaryMinus(),
                    y = hidingOffset.y.unaryMinus()
                )
            )
        }
    }
}
