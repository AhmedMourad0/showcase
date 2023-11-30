package dev.ahmedmourad.showcase.common.screens.milliontimes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

private val EmptyMatrix = createTimeMatrix(null)

@Stable
open class MillionTimesViewModel(handle: Handle) : ViewModel(handle) {
    val state = MillionTimesState()
}

@Stable
class MillionTimesState {
    @Stable
    val start: Instant = Clock.System.now()
    @Stable
    val matrix = UIMatrix(
        width = EmptyMatrix.rows.first().nodes.size,
        height = EmptyMatrix.rows.size
    )
}

@Stable
class UIMatrix(val width: Int, val height: Int) {
    @Stable
    val rows = List(height) {
        UIMatrixRow(width)
    }
}

@Stable
class UIMatrixRow(val width: Int) {
    @Stable
    val nodes = List(width) {
        UIMatrixNode()
    }
}

@Stable
class UIMatrixNode {
    @Stable
    val firstAngle = Animatable(Digits.EMPTY.firstAngle)
    @Stable
    val secondAngle = Animatable(Digits.EMPTY.secondAngle)
}

suspend fun UIMatrix.animateTo(matrix: Matrix, scope: CoroutineScope) {
    repeat(this.height) { rowIndex ->
        repeat(this.width) { nodeIndex ->
            val uiNode = this.rows[rowIndex].nodes[nodeIndex]
            val matrixNode = matrix.rows[rowIndex].nodes[nodeIndex]
            scope.launch {
                uiNode.firstAngle.animateTo(matrixNode.firstAngle, tween(700))
            }
            scope.launch {
                uiNode.secondAngle.animateTo(matrixNode.secondAngle, tween(700))
            }
        }
    }
}
