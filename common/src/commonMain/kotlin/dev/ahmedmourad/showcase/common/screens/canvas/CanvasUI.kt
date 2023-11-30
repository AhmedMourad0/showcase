package dev.ahmedmourad.showcase.common.screens.canvas

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import dev.ahmedmourad.showcase.common.screens.canvas.focus.FocusManager
import dev.ahmedmourad.showcase.common.screens.canvas.models.*
import dev.ahmedmourad.showcase.common.screens.canvas.undo.CanvasAction
import dev.ahmedmourad.showcase.common.screens.canvas.undo.UndoManager
import dev.ahmedmourad.showcase.common.screens.canvas.utils.findQuickActionsLayoutInfo
import dev.ahmedmourad.showcase.common.screens.canvas.utils.rotateBy
import dev.ahmedmourad.showcase.common.screens.canvas.utils.times
import dev.ahmedmourad.showcase.common.screens.canvas.utils.toRect
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.launchBlocker
import dev.ahmedmourad.showcase.common.compose.modifiers.boundedAspectRatio
import dev.ahmedmourad.showcase.common.utils.toPx
import dev.ahmedmourad.showcase.common.screens.canvas.models.CanvasItem
import dev.ahmedmourad.showcase.common.screens.canvas.models.Position
import dev.ahmedmourad.showcase.common.screens.canvas.models.QuickActionsLayoutInfo
import dev.ahmedmourad.showcase.common.screens.canvas.models.containsInside
import dev.ahmedmourad.showcase.common.screens.canvas.models.x
import dev.ahmedmourad.showcase.common.screens.canvas.models.y

const val CANVAS_WIDTH_RATIO = 2f
const val CANVAS_HEIGHT_RATIO = 3f
const val CANVAS_ASPECT_RATIO = CANVAS_WIDTH_RATIO / CANVAS_HEIGHT_RATIO

//TODO: the blocking
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CanvasUI(
    state: CanvasState,
    modifier: Modifier = Modifier
) {
    val focusedItem = remember(state.focusManager.focus, state.items) {
        state.items.firstOrNull { it.canvasId == state.focusManager.focus?.canvasId }
    }
    Box(modifier
        .fillMaxWidth()
        .boundedAspectRatio(CANVAS_ASPECT_RATIO)
        .onGloballyPositioned { state.canvasSize = it.size }
        .background(Color.White, RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))
        .transformable(state = rememberTransformableItemState(
            item = focusedItem,
            focusManager = state.focusManager
        ), canPan = { false }, enabled = state.focusManager.focus != null)
        .itemGesturesEndDetector(
            item = focusedItem,
            focusManager = state.focusManager,
            undoManager = state.undoManager
        ).itemSelector(state)
        .clipToBounds()
    ) {
        state.items.forEach { item ->
            key(item.canvasId) {
                CanvasItem(
                    item = item,
                    state = state,
                    modifier = Modifier.zIndex(item.z.toFloat())
                )
            }
        }
        BottomActionBar(
            state = state,
            modifier = Modifier.align(Alignment.BottomEnd).zIndex(Float.MAX_VALUE)
        )
    }
}

@Suppress("UnusedReceiverParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.CanvasItem(
    item: CanvasItem,
    state: CanvasState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.graphicsLayer {
        this.translationX = item.x
        this.translationY = item.y
        this.scaleX = item.scaleX
        this.scaleY = item.scaleY
        this.rotationZ = item.rotationZ
    }.draggableItem(
        item = item,
        focusManager = state.focusManager
    ).itemGesturesEndDetector(
        item = item,
        focusManager = state.focusManager,
        undoManager = state.undoManager
    ).transformable(state = rememberTransformableItemState(
        item = item,
        focusManager = state.focusManager
    ), canPan = { false })
        .size(item.width, item.height)
        .background(Color.Yellow, RoundedCornerShape(8.dp))
        .border(2.dp, Color.Blue, RoundedCornerShape(8.dp))
    )
//    ItemQuickActions(
//        item = item,
//        focusManager = focusManager,
//        canvasSize = canvasSize,
//        onRemove = {
//            undoManager.register(CanvasAction.Remove(canvasId = item.canvasId))
//            removeItem.invoke(item.canvasId)
//        }
//    )
}

@Composable
private fun ItemQuickActions(
    item: CanvasItem,
    state: CanvasState
) {
    val density = LocalDensity.current
    val actionButtonSize = 44.dp
    val actionButtonSpacing = 8.dp
    val showActionButtons = remember(state.focusManager.focus, item.canvasId) {
        val focus = state.focusManager.focus
        focus?.canvasId == item.canvasId && !focus.isTransforming && focus.requestedActions
    }
    //TODO: item doesn't really work as a key
    val actionButtonsLayoutInfo = remember(item, state.canvasSize, density) {
        findQuickActionsLayoutInfo(
            allowedSpace = Rect(
                offset = Offset(0f, 0f),
                size = state.canvasSize.toSize()
            ), itemRect = item.toRect(density),
            neededSpace = Size(
                width = actionButtonSize.toPx(density),
                height = actionButtonSize.plus(actionButtonSpacing).toPx(density)
            ), spacing = actionButtonSpacing.toPx(density)
        )
    }
    //why a button for each position value? because a single button gets possessed when you move
    // the item around a corner
    Position.entries.forEach { position ->
        key(position) {
            val isActive = remember(showActionButtons, actionButtonsLayoutInfo.position) {
                showActionButtons && position == actionButtonsLayoutInfo.position
            }
            ItemQuickAction(
                isActive = isActive,
                size = actionButtonSize,
                layoutInfo = actionButtonsLayoutInfo,
                onClick = { state.items = state.items.filter { it.canvasId != item.canvasId } },
                modifier = Modifier.zIndex(
                    if (isActive) Float.MAX_VALUE else item.z.toFloat().minus(0.1f)
                )
            )
        }
    }
}

@Composable
private fun ItemQuickAction(
    isActive: Boolean,
    size: Dp,
    layoutInfo: QuickActionsLayoutInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val duration = 200
    val enterAnim = remember {
        slideIn(tween(duration)) {
            layoutInfo.animationOffset
        } + fadeIn(tween(duration)) + scaleIn(tween(duration))
    }
    val exitAnim = remember {
        slideOut(tween(duration)) {
            layoutInfo.animationOffset
        } + fadeOut(tween(duration)) + scaleOut(tween(duration))
    }
    AnimatedVisibility(
        visible = isActive,
        enter = enterAnim,
        exit = exitAnim,
        modifier = modifier.graphicsLayer {
            translationX = layoutInfo.x
            translationY = layoutInfo.y
        }
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(size)
        ) {
            Image(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun BottomActionBar(
    state: CanvasState,
    modifier: Modifier = Modifier
) {
    Row(modifier.padding(12.dp)) {
        val scope = rememberCoroutineScope()
        CanvasActionButton(
            icon = Icons.Rounded.Undo,
            enabled = { state.undoManager.hasUndoHistory() }
        ) {
            scope.launchBlocker {
                state.items = state.undoManager.undo(state.items)
            }
        }
        Spacer(Modifier.width(16.dp))
        CanvasActionButton(
            icon = Icons.Rounded.Redo,
            enabled = { state.undoManager.hasRedoHistory() }
        ) {
            scope.launchBlocker {
                state.items = state.undoManager.redo(state.items)
            }
        }
    }
}

@Composable
private fun CanvasActionButton(
    icon: ImageVector,
    enabled: () -> Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick,
        enabled = Showcase.acceptsInputs && enabled(),
        modifier = modifier.graphicsLayer {
            alpha = if (enabled()) 1f else 0.3f
        }
    ) {
        Icon(icon, null, tint = Color.Black)
    }
}

@Composable
private fun rememberTransformableItemState(
    item: CanvasItem?,
    focusManager: FocusManager
) = rememberTransformableState { zoomChange, _, rotationChange ->
    if (item != null && focusManager.requestTransformation(item.canvasId)) {
        item.apply {
            scaleX *= zoomChange
            scaleY *= zoomChange
            rotationZ += rotationChange
        }
    }
}

private fun Modifier.itemGesturesEndDetector(
    item: CanvasItem?,
    focusManager: FocusManager,
    undoManager: UndoManager
) = onGesturesEnd {
    if (item != null && focusManager.endTransformation(item.canvasId)) {
        undoManager.register(CanvasAction.Transform(item))
    }
}

private fun Modifier.onGesturesEnd(action: () -> Unit) = pointerInput("GestureEndDetector") {
    awaitEachGesture {
        do {
            val event = awaitPointerEvent()
            if (event.changes.none { it.pressed }) {
                action.invoke()
            }
        } while (event.changes.any { it.pressed })
    }
}

private fun Modifier.draggableItem(
    item: CanvasItem?,
    focusManager: FocusManager
) = pointerInput("DragDetector") {
    detectDragGestures(onDrag = { _, dragAmount ->
        if (item != null && focusManager.requestTransformation(item.canvasId)) {
            item.offset += dragAmount
                .rotateBy(item.rotationZ)
                .times(Offset(item.scaleX, item.scaleY))
        }
    })
}

private fun Modifier.itemSelector(
    state: CanvasState
) = pointerInput("OnClick") {
    detectTapGestures { clickOffset ->
        val item = state.items.filter {
            it.toRect(this@pointerInput).containsInside(clickOffset)
        }.minByOrNull {
            it.z
        } ?: return@detectTapGestures
        state.focusManager.onItemClicked(item.canvasId)
        val maxZ = state.items.maxOf { it.z }
        if (maxZ > item.z) {
            item.z = maxZ + 1
            state.undoManager.register(CanvasAction.UpdateZ(canvasId = item.canvasId, maxZ + 1))
        }
    }
}
