package dev.ahmedmourad.showcase.common.canvas

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.ViewModel
import dev.ahmedmourad.showcase.common.canvas.focus.FCFSFocusManager
import dev.ahmedmourad.showcase.common.canvas.focus.FocusManager
import dev.ahmedmourad.showcase.common.canvas.models.CanvasItem
import dev.ahmedmourad.showcase.common.canvas.undo.UndoManager

@Stable
open class CanvasViewModel(
    val handle: Handle
) : ViewModel(handle) {
    val state = CanvasState()
    init {
        doOnDispose {
            state.undoManager.dispose()
        }
    }
}

@Stable
class CanvasState {
    var items by mutableStateOf(List(4) { randomItem(it) })
    var canvasSize by mutableStateOf(IntSize.Zero)
    val undoManager = UndoManager { items }
    val focusManager: FocusManager = FCFSFocusManager()
}

private fun randomItem(z: Int) = CanvasItem(
    imageUrl = "https://duckduckgo.com/?q=vehicula",
    width = 100.dp,
    height = 100.dp,
    offset = Offset.Zero,
    z = z,
    rotation = 0f,
    scaleX = 1f,
    scaleY = 1f,
    flip = false
)
