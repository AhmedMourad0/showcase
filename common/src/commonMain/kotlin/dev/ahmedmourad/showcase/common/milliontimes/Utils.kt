package dev.ahmedmourad.showcase.common.milliontimes

import androidx.compose.runtime.Stable
import dev.ahmedmourad.showcase.common.utils.segregated
import kotlin.math.PI
import kotlin.time.Duration

@Stable
fun createTimeMatrix(from: Duration?): Matrix {
    val duration = from?.segregated() ?: return TimeMatrix(
        Digits.from(null),
        Digits.from(null),
        Digits.from(null),
        Digits.from(null)
    )
    val firstMinute = duration.minutes / 10
    val secondMinute = duration.minutes % 10
    val firstSecond = duration.seconds / 10
    val secondSecond = duration.seconds % 10
    return TimeMatrix(
        Digits.from(firstMinute.toInt()),
        Digits.from(secondMinute.toInt()),
        Digits.from(firstSecond.toInt()),
        Digits.from(secondSecond.toInt())
    )
}

@Stable
private fun TimeMatrix(
    topLeft: Matrix,
    topRight: Matrix,
    bottomLeft: Matrix,
    bottomRight: Matrix
) = topLeft.concatHorizontal(topRight).concatVertical(bottomLeft.concatHorizontal(bottomRight))

@Stable
fun Float.rad() = this / 180 * PI.toFloat()
