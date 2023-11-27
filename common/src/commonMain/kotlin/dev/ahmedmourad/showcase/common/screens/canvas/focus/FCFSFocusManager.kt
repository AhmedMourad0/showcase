package dev.ahmedmourad.showcase.common.screens.canvas.focus

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Starting a transformation automatically captures the focus until the transformation is completed
 * Clicking on a focused item clears the focus
 * Clicking on a non-focused item while focus is cleared or isTransforming is false captures the focus
 * Focus can't be transferred while a transformation is happening
 */
@Stable
class FCFSFocusManager : FocusManager {

    override var focus by mutableStateOf<CanvasFocus?>(null)
        private set

    override fun onItemClicked(canvasId: String) {
        val focus = focus
        this.focus = when {
            focus != null && focus.isTransforming -> return
            focus?.canvasId == canvasId -> focus.copy(requestedActions = !focus.requestedActions)
            else -> CanvasFocus(
                canvasId = canvasId,
                requestedActions = true,
                isTransforming = false
            )
        }
    }

    override fun requestTransformation(canvasId: String): Boolean {
        val focus = focus
        return when {
            focus?.canvasId == canvasId -> {
                if (!focus.isTransforming) {
                    this.focus = focus.copy(isTransforming = true)
                }
                true
            }
            focus?.isTransforming == true -> false
            else -> {
                this.focus = CanvasFocus(
                    canvasId = canvasId,
                    requestedActions = false,
                    isTransforming = true
                )
                true
            }
        }
    }

    override fun endTransformation(canvasId: String): Boolean {
        val focus = focus
        if (focus?.canvasId == canvasId && focus.isTransforming) {
            this.focus = focus.copy(isTransforming = false)
            return true
        }
        return false
    }

    override fun transferFocus(canvasId: String) {
        focus = CanvasFocus(
            canvasId = canvasId,
            requestedActions = false,
            isTransforming = false
        )
    }

    override fun clearFocus() {
        focus = null
    }
}
