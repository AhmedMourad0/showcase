package dev.ahmedmourad.showcase.common.screens.bungeegumbars

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
private enum class Head {
    Start, End
}

@Composable
fun BungeeGumRangeBar(
    value: () -> FloatRange,
    onValueChange: (FloatRange) -> Unit,
    range: () -> FloatRange,
    modifier: Modifier = Modifier,
    minBarThickness: Dp = 6.dp,
    lineThickness: Dp = 1.dp,
    barColor: Color = MaterialTheme.colorScheme.primary,
    lineColor: Color = Color.Gray
) {
    @Suppress("NAME_SHADOWING") val value by rememberUpdatedState(value())
    @Suppress("NAME_SHADOWING") val range by rememberUpdatedState(range())
    @Suppress("NAME_SHADOWING") val onValueChange by rememberUpdatedState(onValueChange)

    var size by remember { mutableStateOf(Size.Zero) }

    val headRadius by remember {
        derivedStateOf {
            size.height / 2
        }
    }
    val lineWidth by remember {
        derivedStateOf {
            size.width - headRadius * 2
        }
    }
    val startOffset by remember {
        derivedStateOf {
            headRadius + (value.start - range.start) / (range.end - range.start) * lineWidth
        }
    }
    val endOffset by remember {
        derivedStateOf {
            headRadius + (value.end - range.start) / (range.end - range.start) * lineWidth
        }
    }

    val path = remember { Path() }
    var focusedHead by remember { mutableStateOf<Head?>(null) }

    Canvas(modifier = modifier.pointerInput("Horizontal Drag Input", size) {
        detectHorizontalDragGestures(onDragStart = {
            focusedHead = null
        }, onDragEnd = {
            focusedHead = null
        }, onDragCancel = {
            focusedHead = null
        }, onHorizontalDrag = { change, dragAmount ->
            if (focusedHead == null) {
                focusedHead = when {
                    value.start == value.end -> if (dragAmount > 0) Head.End else Head.Start
                    change.position.x <= startOffset -> Head.Start
                    change.position.x >= endOffset -> Head.End
                    change.position.x - startOffset < endOffset - change.position.x -> Head.Start
                    else -> Head.End
                }
            }
            when (focusedHead) {
                Head.Start -> {
                    val new = range.start + (change.position.x - headRadius) / lineWidth * (range.end - range.start)
                    onValueChange(value.copy(start = new.coerceIn(range.start, value.end)))
                }
                Head.End -> {
                    val new = range.start + (change.position.x - headRadius) / lineWidth * (range.end - range.start)
                    onValueChange(value.copy(end = new.coerceIn(value.start, range.end)))
                }
                null -> Unit
            }
            change.consume()
        })
    }.pointerInput("Click Input", size) {
        detectTapGestures {
            when {
                it.x <= headRadius -> onValueChange(value.copy(start = range.start))
                it.x >= lineWidth + headRadius -> onValueChange(value.copy(end = range.end))
                (it.x <= startOffset) || it.x - startOffset < endOffset - it.x -> {
                    val new = range.start + (it.x - headRadius) / lineWidth * (range.end - range.start)
                    onValueChange(value.copy(start = new.coerceIn(range.start, value.end)))
                }
                else -> {
                    val new = range.start + (it.x - headRadius) / lineWidth * (range.end - range.start)
                    onValueChange(value.copy(end = new.coerceIn(value.start, range.end)))
                }
            }
        }
    }) {
        size = this.size
        val center = size.center
        val valueCenter = (endOffset + startOffset) / 2
        val consumedRatio = (endOffset - startOffset) / lineWidth
        val thickness = minBarThickness.value + (1f - consumedRatio) * (headRadius * 2 - minBarThickness.value)

        val leftArcRect = Rect(Offset(startOffset, center.y), headRadius)
        val rightArcRect = Rect(Offset(endOffset, center.y), headRadius)
        drawLine(
            lineColor,
            center.copy(x = headRadius),
            center.copy(x = size.width - headRadius),
            lineThickness.value
        )
        path.apply {
            reset()
            moveTo(startOffset, size.height)
            arcTo(leftArcRect, 90f, 180f, false)
            cubicTo(
                startOffset + headRadius,
                0f,
                startOffset,
                center.y - thickness / 2,
                valueCenter,
                center.y - thickness / 2
            )
            cubicTo(
                endOffset,
                center.y - thickness / 2,
                endOffset - headRadius,
                0f,
                endOffset,
                0f
            )
            arcTo(rightArcRect, 270f, 180f, false)
            cubicTo(
                endOffset - headRadius,
                size.height,
                endOffset,
                center.y + thickness / 2,
                valueCenter,
                center.y + thickness / 2
            )
            cubicTo(
                startOffset,
                center.y + thickness / 2,
                startOffset + headRadius,
                size.height,
                startOffset,
                size.height
            )
            close()
        }
        drawPath(path, barColor)
    }
}
