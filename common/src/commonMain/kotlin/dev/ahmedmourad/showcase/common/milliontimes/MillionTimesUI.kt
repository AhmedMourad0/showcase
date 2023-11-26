package dev.ahmedmourad.showcase.common.milliontimes

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MillionTimesUI(
    state: () -> MillionTimesState,
    onStateChange: (MillionTimesState) -> Unit,
    modifier: Modifier = Modifier
) {
    Matrix(
        value = { state().matrix },
        modifier = modifier.padding(8.dp)
    )
}

@Composable
private fun Matrix(value: () -> Matrix, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(modifier.fillMaxWidth()) {
            value().rows.forEachIndexed { rowIndex, row ->
                Row(Modifier.fillMaxWidth()) {
                    row.nodes.forEachIndexed { nodeIndex, node ->
                        key(rowIndex, nodeIndex) {
                            MatrixNode(node, Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatrixNode(
    node: MatrixNode,
    modifier: Modifier = Modifier,
    frameColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
    handColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val angle1 by animateFloatAsState(
        targetValue = node.firstAngle.unaryMinus().toFloat().rad(),
        animationSpec = tween(700)
    )
    val angle2 by animateFloatAsState(
        targetValue = node.secondAngle.unaryMinus().toFloat().rad(),
        animationSpec = tween(700)
    )
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val center = size.center
        val radius = size.minDimension / 2
        drawCircle(frameColor, radius, center, style = Stroke(2f))
        val newX1 = center.x + radius * cos(angle1)
        val newY1 = center.y + radius * sin(angle1)
        drawLine(handColor, center, Offset(newX1, newY1), 3f)
        val newX2 = center.x + radius * cos(angle2)
        val newY2 = center.y + radius * sin(angle2)
        drawLine(handColor, center, Offset(newX2, newY2), 3f)
    }
}
