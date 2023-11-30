package dev.ahmedmourad.showcase.common.screens.milliontimes

import androidx.compose.runtime.Stable

object Digits {

    @Stable
    fun from(digit: Int?): Matrix {
        return when (digit) {
            null -> Dead
            0 -> Zero
            1 -> One
            2 -> Two
            3 -> Three
            4 -> Four
            5 -> Five
            6 -> Six
            7 -> Seven
            8 -> Eight
            9 -> Nine
            else -> throw IllegalArgumentException("Digit must be between 0 and 9: $digit")
        }
    }
    
    private val TopLeftCorner = MatrixNode(0f, 270f)
    private val TopRightCorner = MatrixNode(180f, 270f)
    private val BottomLeftCorner = MatrixNode(90f, 0f)
    private val BottomRightCorner = MatrixNode(90f, 180f)
    private val Horizontal = MatrixNode(0f, 180f)
    private val Vertical = MatrixNode(90f, 270f)
    val Empty = MatrixNode(225f, 225f)

    private val Dead = Matrix(
        MatrixRow(
            Empty,
            Empty,
            Empty,
            Empty,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Empty,
            Empty,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Empty,
            Empty,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Empty,
            Empty,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Empty,
            Empty,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Empty,
            Empty,
            Empty
        )
    )

    private val Zero = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            Vertical,
            Vertical,
            Empty,
            Vertical,
            Vertical
        ), MatrixRow(
            Vertical,
            Vertical,
            Empty,
            Vertical,
            Vertical
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            BottomRightCorner
        )
    )

    private val One = Matrix(
        MatrixRow(
            Empty,
            TopLeftCorner,
            Horizontal,
            TopRightCorner,
            Empty
        ), MatrixRow(
            Empty,
            BottomLeftCorner,
            TopRightCorner,
            Vertical,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Vertical,
            Vertical,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Vertical,
            Vertical,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Vertical,
            Vertical,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            BottomLeftCorner,
            BottomRightCorner,
            Empty
        )
    )

    private val Two = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            BottomRightCorner
        )
    )

    private val Three = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            BottomRightCorner
        )
    )

    private val Four = Matrix(
        MatrixRow(
            TopLeftCorner,
            TopRightCorner,
            Empty,
            Empty,
            Empty
        ), MatrixRow(
            Vertical,
            Vertical,
            Empty,
            Empty,
            Empty
        ), MatrixRow(
            Vertical,
            Vertical,
            TopLeftCorner,
            TopRightCorner,
            Empty
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            BottomRightCorner,
            BottomLeftCorner,
            TopRightCorner
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            TopRightCorner,
            TopLeftCorner,
            BottomRightCorner
        ), MatrixRow(
            Empty,
            Empty,
            BottomLeftCorner,
            BottomRightCorner,
            Empty
        )
    )

    private val Five = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            BottomRightCorner
        )
    )

    private val Six = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            BottomRightCorner
        )
    )

    private val Seven = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            MatrixNode(180f, 225f),
            MatrixNode(90f, 225f)
        ), MatrixRow(
            Empty,
            Empty,
            MatrixNode(45f, 270f),
            MatrixNode(45f, 270f),
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Vertical,
            Vertical,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            Vertical,
            Vertical,
            Empty
        ), MatrixRow(
            Empty,
            Empty,
            BottomLeftCorner,
            BottomRightCorner,
            Empty
        )
    )

    private val Eight = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            BottomRightCorner
        )
    )

    private val Nine = Matrix(
        MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            TopRightCorner
        ), MatrixRow(
            Vertical,
            TopLeftCorner,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            Vertical,
            BottomLeftCorner,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            TopRightCorner,
            Vertical
        ), MatrixRow(
            TopLeftCorner,
            Horizontal,
            Horizontal,
            BottomRightCorner,
            Vertical
        ), MatrixRow(
            BottomLeftCorner,
            Horizontal,
            Horizontal,
            Horizontal,
            BottomRightCorner
        )
    )
}
