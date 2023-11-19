package dev.ahmedmourad.showcase.common.canvas.focus

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class CanvasFocus(
    val canvasId: String,
    val requestedActions: Boolean,
    val isTransforming: Boolean
)

@Stable
interface FocusManager {
    val focus: CanvasFocus?
    fun onItemClicked(canvasId: String)
    fun requestTransformation(canvasId: String): Boolean
    fun endTransformation(canvasId: String): Boolean
    fun transferFocus(canvasId: String)
    fun clearFocus()
}
