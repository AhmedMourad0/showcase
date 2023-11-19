package dev.ahmedmourad.showcase.common.canvas.undo

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.Snapshot
import dev.ahmedmourad.showcase.common.canvas.models.CanvasItem

@Stable
class UndoManager(
    private val items: () -> List<CanvasItem>
) {

    private val undoHistory = mutableListOf<CanvasAction>()
    private val redoHistory = mutableListOf<CanvasAction>()
    private var snapshot: Snapshot = Snapshot.takeSnapshot()

    fun register(action: CanvasAction) {
        val invertedAction = snapshot.enter {
            action.invert(when (action) {
                is CanvasAction.Add -> action.item
                is CanvasAction.Remove -> items().first { it.canvasId == action.canvasId }
                is CanvasAction.TransformativeAction -> items().first { it.canvasId == action.canvasId }
            })
        }
        undoHistory.add(invertedAction)
        redoHistory.clear()
        takeSnapshot()
    }

    suspend fun undo(items: List<CanvasItem>): List<CanvasItem> = applyAndMove(
        from = undoHistory,
        to = redoHistory,
        items = items
    ).also { takeSnapshot() }

    suspend fun redo(items: List<CanvasItem>): List<CanvasItem> = applyAndMove(
        from = redoHistory,
        to = undoHistory,
        items = items
    ).also { takeSnapshot() }

    fun hasUndoHistory() = undoHistory.isNotEmpty()

    fun hasRedoHistory() = redoHistory.isNotEmpty()

    fun dispose() = snapshot.dispose()

    private fun takeSnapshot() {
        snapshot.dispose()
        snapshot = Snapshot.takeSnapshot()
    }
}

private suspend fun applyAndMove(
    from: MutableList<CanvasAction>,
    to: MutableList<CanvasAction>,
    items: List<CanvasItem>
): List<CanvasItem> {
    if (from.isEmpty()) return items
    return when (val action = from.removeLast()) {

        is CanvasAction.Add -> {
            to.add(action.invert(action.item))
            items + action.item
        }

        is CanvasAction.Remove -> {
            val item = items.last()
            to.add(action.invert(item))
            items.minus(item)
        }

        is CanvasAction.TransformativeAction -> {
            items.forEach { item ->
                if (item.canvasId == action.canvasId) {
                    to.add(action.invert(item))
                    action.applyTo(item)
                }
            }
            items
        }
    }
}

private suspend fun CanvasAction.TransformativeAction.applyTo(
    item: CanvasItem
) = when (this) {
    is CanvasAction.Transform -> {
        item.offset = this.offset
        item.z = this.z
        item.rotationZ = this.rotationZ
        item.scaleX = this.scaleX
        item.scaleY = this.scaleY
    }
    is CanvasAction.Flip -> item.flip = !item.flip
    is CanvasAction.UpdateZ -> item.z = this.z
}

private fun CanvasAction.invert(oldItem: CanvasItem): CanvasAction = when (this) {
    is CanvasAction.Add -> CanvasAction.Remove(oldItem.canvasId)
    is CanvasAction.Remove -> CanvasAction.Add(oldItem)
    is CanvasAction.Transform -> CanvasAction.Transform(oldItem)
    is CanvasAction.Flip -> CanvasAction.Flip(canvasId = oldItem.canvasId)
    is CanvasAction.UpdateZ -> CanvasAction.UpdateZ(
        canvasId = oldItem.canvasId,
        z = oldItem.z
    )
}
