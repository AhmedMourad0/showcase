package dev.ahmedmourad.showcase.common.screens.milliontimes

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
import dev.ahmedmourad.showcase.common.screens.home.CarouselState
import dev.ahmedmourad.showcase.common.utils.tickerFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MillionTimesUI(
    state: MillionTimesState,
    carouselState: CarouselState,
    modifier: Modifier = Modifier
) {
    Matrix(
        value = state.matrix,
        modifier = modifier.padding(8.dp)
    )
    LaunchedEffect(carouselState) {
        snapshotFlow { carouselState }
            .distinctUntilChanged()
            .flatMapLatest {
                when (it) {
                    CarouselState.Collapsed -> flowOf(createTimeMatrix(null))
                    CarouselState.Expanded -> {
                        val start = Clock.System.now()
                        tickerFlow(
                            period = 1.seconds,
                            initialDelay = 1.seconds
                        ).map { createTimeMatrix(Clock.System.now().minus(start)) }
                    }
                }
            }.flowOn(Dispatchers.Default)
            .onEach { state.matrix.animateTo(it, this) }
            .flowOn(Dispatchers.Main)
            .launchIn(this)
    }
}

@Composable
private fun Matrix(value: UIMatrix, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(modifier.fillMaxWidth()) {
            value.rows.forEachIndexed { rowIndex, row ->
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
    node: UIMatrixNode,
    modifier: Modifier = Modifier,
    frameColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
    handColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val center = size.center
        val radius = size.minDimension / 2
        val angle1 = node.firstAngle.value.unaryMinus().rad()
        val angle2 = node.secondAngle.value.unaryMinus().rad()
        drawCircle(frameColor, radius, center, style = Stroke(2f))
        val newX1 = center.x + radius * cos(angle1)
        val newY1 = center.y + radius * sin(angle1)
        drawLine(handColor, center, Offset(newX1, newY1), 3f)
        val newX2 = center.x + radius * cos(angle2)
        val newY2 = center.y + radius * sin(angle2)
        drawLine(handColor, center, Offset(newX2, newY2), 3f)
    }
}
