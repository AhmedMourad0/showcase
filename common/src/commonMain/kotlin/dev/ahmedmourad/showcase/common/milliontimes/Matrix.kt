package dev.ahmedmourad.showcase.common.milliontimes

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.ahmedmourad.showcase.common.Parcelable
import dev.ahmedmourad.showcase.common.Parcelize
import kotlin.jvm.JvmInline

@Parcelize
@JvmInline
@Immutable
value class Matrix(val rows: List<MatrixRow>) : Parcelable {
    constructor(vararg rows: MatrixRow) : this(rows.toList())
}

@Parcelize
@Immutable
data class MatrixRow(val nodes: List<MatrixNode>) : Parcelable {
    constructor(vararg nodes: MatrixNode) : this(nodes.toList())
}

@Parcelize
@Immutable
data class MatrixNode(
    val firstAngle: Int,
    val secondAngle: Int
) : Parcelable

@Stable
fun Matrix.concatVertical(other: Matrix): Matrix {
    return Matrix(this.rows + other.rows)
}

@Stable
fun Matrix.concatHorizontal(other: Matrix): Matrix {
    return Matrix(this.rows.zip(other.rows).map {
        MatrixRow(it.first.nodes + it.second.nodes)
    })
}
