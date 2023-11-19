package dev.ahmedmourad.showcase.common

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

@Composable
actual fun OptionalDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    mobileType: ModelType,
    desktopType: ModelType,
    content: @Composable () -> Unit
) {
    when (desktopType) {
        ModelType.DropdownMenu -> DropdownMenu(
            expanded = show,
            onDismissRequest = onDismissRequest,
            content = { content() }
        )
        ModelType.Dialog -> if (show) {
            Dialog(
                onCloseRequest = onDismissRequest,
                content = { content() }
            )
        }
        is ModelType.Popup -> if (show) {
            Popup(
                popupPositionProvider = AlignmentOffsetPositionProvider(Alignment.TopStart, IntOffset.Zero),
                onDismissRequest = onDismissRequest,
                focusable = true,
                content = {
                    Surface(shadowElevation = 6.dp, modifier = desktopType.modifier) { content() }
                }
            )
        }
        is ModelType.BottomSheet -> Unit //TODO
    }
}

class AlignmentOffsetPositionProvider(
    val alignment: Alignment,
    val offset: IntOffset
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // TODO: Decide which is the best way to round to result without reimplementing Alignment.align
        var popupPosition = IntOffset(0, 0)

        // Get the aligned point inside the parent
        val parentAlignmentPoint = alignment.align(
            IntSize.Zero,
            IntSize(anchorBounds.width, anchorBounds.height),
            layoutDirection
        )
        // Get the aligned point inside the child
        val relativePopupPos = alignment.align(
            IntSize.Zero,
            IntSize(popupContentSize.width, popupContentSize.height),
            layoutDirection
        )
        return anchorBounds.bottomLeft

        // Add the position of the parent
        popupPosition += IntOffset(anchorBounds.left, anchorBounds.top)

        // Add the distance between the parent's top left corner and the alignment point
        popupPosition += parentAlignmentPoint

        // Subtract the distance between the children's top left corner and the alignment point
        popupPosition -= IntOffset(relativePopupPos.x, relativePopupPos.y)

        // Add the user offset
        val resolvedOffset = IntOffset(
            offset.x * (if (layoutDirection == LayoutDirection.Ltr) 1 else -1),
            offset.y
        )
        popupPosition += resolvedOffset

        return popupPosition
    }
}
