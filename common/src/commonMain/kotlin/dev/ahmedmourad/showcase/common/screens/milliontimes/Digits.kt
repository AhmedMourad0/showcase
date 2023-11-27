package dev.ahmedmourad.showcase.common.screens.milliontimes

import androidx.compose.runtime.Stable

object Digits {

    @Stable
    fun from(digit: Int?): Matrix {
        return when (digit) {
            null -> DEAD
            0 -> ZERO
            1 -> ONE
            2 -> TWO
            3 -> THREE
            4 -> FOUR
            5 -> FIVE
            6 -> SIX
            7 -> SEVEN
            8 -> EIGHT
            9 -> NINE
            else -> throw IllegalArgumentException("Digit must be between 0 and 9: $digit")
        }
    }
    
    private val TOP_LEFT_CORNER = MatrixNode(0, 270)
    private val TOP_RIGHT_CORNER = MatrixNode(180, 270)
    private val BOTTOM_LEFT_CORNER = MatrixNode(90, 0)
    private val BOTTOM_RIGHT_CORNER = MatrixNode(90, 180)
    private val HORIZONTAL = MatrixNode(0, 180)
    private val VERTICAL = MatrixNode(90, 270)
    private val EMPTY = MatrixNode(225, 225)

    private val DEAD = Matrix(
        MatrixRow(
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY,
            EMPTY
        )
    )

    private val ZERO = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            VERTICAL,
            EMPTY,
            VERTICAL,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            VERTICAL,
            EMPTY,
            VERTICAL,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        )
    )

    private val ONE = Matrix(
        MatrixRow(
            EMPTY,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            EMPTY
        ), MatrixRow(
            EMPTY,
            BOTTOM_LEFT_CORNER,
            TOP_RIGHT_CORNER,
            VERTICAL,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            VERTICAL,
            VERTICAL,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            VERTICAL,
            VERTICAL,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            VERTICAL,
            VERTICAL,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            BOTTOM_LEFT_CORNER,
            BOTTOM_RIGHT_CORNER,
            EMPTY
        )
    )

    private val TWO = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        )
    )

    private val THREE = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        )
    )

    private val FOUR = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            TOP_RIGHT_CORNER,
            EMPTY,
            EMPTY,
            EMPTY
        ), MatrixRow(
            VERTICAL,
            VERTICAL,
            EMPTY,
            EMPTY,
            EMPTY
        ), MatrixRow(
            VERTICAL,
            VERTICAL,
            TOP_LEFT_CORNER,
            TOP_RIGHT_CORNER,
            EMPTY
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            BOTTOM_RIGHT_CORNER,
            BOTTOM_LEFT_CORNER,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            TOP_LEFT_CORNER,
            BOTTOM_RIGHT_CORNER
        ), MatrixRow(
            EMPTY,
            EMPTY,
            BOTTOM_LEFT_CORNER,
            BOTTOM_RIGHT_CORNER,
            EMPTY
        )
    )

    private val FIVE = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        )
    )

    private val SIX = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        )
    )

    private val SEVEN = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            MatrixNode(180, 225),
            MatrixNode(90, 225)
        ), MatrixRow(
            EMPTY,
            EMPTY,
            MatrixNode(45, 270),
            MatrixNode(45, 270),
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            VERTICAL,
            VERTICAL,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            VERTICAL,
            VERTICAL,
            EMPTY
        ), MatrixRow(
            EMPTY,
            EMPTY,
            BOTTOM_LEFT_CORNER,
            BOTTOM_RIGHT_CORNER,
            EMPTY
        )
    )

    private val EIGHT = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        )
    )

    private val NINE = Matrix(
        MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER
        ), MatrixRow(
            VERTICAL,
            TOP_LEFT_CORNER,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            VERTICAL,
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            TOP_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            TOP_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER,
            VERTICAL
        ), MatrixRow(
            BOTTOM_LEFT_CORNER,
            HORIZONTAL,
            HORIZONTAL,
            HORIZONTAL,
            BOTTOM_RIGHT_CORNER
        )
    )
}
