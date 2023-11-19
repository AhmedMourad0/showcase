package dev.ahmedmourad.showcase.common.canvas

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
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.ahmedmourad.showcase.common.canvas.focus.FocusManager
import dev.ahmedmourad.showcase.common.canvas.models.*
import dev.ahmedmourad.showcase.common.canvas.undo.CanvasAction
import dev.ahmedmourad.showcase.common.canvas.undo.UndoManager
import dev.ahmedmourad.showcase.common.canvas.utils.findQuickActionsLayoutInfo
import dev.ahmedmourad.showcase.common.canvas.utils.rotateBy
import dev.ahmedmourad.showcase.common.canvas.utils.times
import dev.ahmedmourad.showcase.common.canvas.utils.toRect
import dev.ahmedmourad.showcase.common.compose.launchBlocker
import dev.ahmedmourad.showcase.common.pickers.toPx

const val CANVAS_WIDTH_RATIO = 2f
const val CANVAS_HEIGHT_RATIO = 3f
const val CANVAS_ASPECT_RATIO = CANVAS_WIDTH_RATIO / CANVAS_HEIGHT_RATIO

//TODO: the blocking
@Composable
fun CanvasUI(
    items: () -> List<CanvasItem>,
    onItemsChange: (List<CanvasItem>) -> Unit,
    undoManager: UndoManager,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize()) {
        Canvas(
            items = { items() },
            onItemsChange = onItemsChange,
            undoManager = undoManager,
            focusManager = focusManager,
            modifier = Modifier.weight(1f)
        )
        Row {
            val scope = rememberCoroutineScope()
            Button(onClick = {
                scope.launchBlocker {
                    onItemsChange(undoManager.undo(items()))
                }
            }) {
                Text("Undo")
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = {
                scope.launchBlocker {
                    onItemsChange(undoManager.redo(items()))
                }
            }) {
                Text("Redo")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Canvas(
    items: () -> List<CanvasItem>,
    onItemsChange: (List<CanvasItem>) -> Unit,
    undoManager: UndoManager,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().background(Color.Black)) {
        val focusedItem = remember(focusManager.focus, items()) {
            items().firstOrNull { it.canvasId == focusManager.focus?.canvasId }
        }
        var canvasSize by remember { mutableStateOf(Size(0f, 0f)) }
        BoxWithConstraints(Modifier
            .fillMaxWidth()
            .aspectRatio(CANVAS_ASPECT_RATIO)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .transformable(state = rememberTransformableItemState(
                item = focusedItem,
                focusManager = focusManager
            ), canPan = { false }, enabled = focusManager.focus != null)
            .itemGesturesEndDetector(
                item = focusedItem,
                focusManager = focusManager,
                undoManager = undoManager
            ).itemSelector(
                items = items,
                focusManager = focusManager,
                undoManager = undoManager
            ).clipToBounds()
        ) {
            LaunchedEffect(constraints) {
                canvasSize = Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
            }
            items().forEach { item ->
                key(item.canvasId) {
                    CanvasItem(
                        item = item,
                        undoManager = undoManager,
                        focusManager = focusManager,
                        removeItem = { canvasId ->
                            onItemsChange(items().filter { it.canvasId != canvasId })
                        }, canvasSize = canvasSize,
                        modifier = Modifier.zIndex(item.z.toFloat())
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.CanvasItem(
    item: CanvasItem,
    removeItem: (canvasId: String) -> Unit,
    undoManager: UndoManager,
    focusManager: FocusManager,
    canvasSize: Size,
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
        focusManager = focusManager
    ).itemGesturesEndDetector(
        item = item,
        focusManager = focusManager,
        undoManager = undoManager
    ).transformable(state = rememberTransformableItemState(
        item = item,
        focusManager = focusManager
    ), canPan = {
        false
    }).size(item.width, item.height).background(Color.Yellow).border(2.dp, Color.Blue))
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
    focusManager: FocusManager,
    canvasSize: Size,
    onRemove: () -> Unit
) {
    val density = LocalDensity.current
    val actionButtonSize = 44.dp
    val actionButtonSpacing = 8.dp
    val showActionButtons = remember(focusManager.focus, item.canvasId) {
        val focus = focusManager.focus
        focus?.canvasId == item.canvasId && !focus.isTransforming && focus.requestedActions
    }
    //TODO: item doesn't really work as a key
    val actionButtonsLayoutInfo = remember(item, canvasSize, density) {
        findQuickActionsLayoutInfo(
            allowedSpace = Rect(
                offset = Offset(0f, 0f),
                size = Size(canvasSize.width, canvasSize.height)
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
                onClick = onRemove,
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
    items: () -> List<CanvasItem>,
    focusManager: FocusManager,
    undoManager: UndoManager
) = pointerInput("OnClick") {
    detectTapGestures { clickOffset ->
        val item = items().minByOrNull { item ->
            item.takeIf {
                it.toRect(this@pointerInput).containsInside(clickOffset)
            }?.z ?: Int.MAX_VALUE
        } ?: return@detectTapGestures
        focusManager.onItemClicked(item.canvasId)
        val maxZ = items().maxOf { it.z }
        if (maxZ > item.z) {
            item.z = maxZ + 1
            undoManager.register(CanvasAction.UpdateZ(canvasId = item.canvasId, maxZ + 1))
        }
    }
}
