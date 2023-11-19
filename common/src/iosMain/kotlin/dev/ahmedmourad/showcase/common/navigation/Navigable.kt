package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.InputBlocker
import dev.ahmedmourad.showcase.common.compose.acceptsInputs
import dev.ahmedmourad.showcase.common.compose.block
import dev.ahmedmourad.showcase.common.compose.components.ActionButton
import dev.ahmedmourad.showcase.common.compose.modifiers.mirror
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.unblock
import dev.ahmedmourad.showcase.common.ios.UIControllerEvent
import dev.ahmedmourad.showcase.common.ios.UIControllerLifecycleEffect
import dev.ahmedmourad.showcase.common.pickers.toDp
import dev.ahmedmourad.showcase.common.pickers.toPx
import dev.ahmedmourad.showcase.common.pickers.unsalable
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private val ActionButtonSize = 32.dp
private val ActionButtonsSpacing = 8.dp
private val BackIconWidth = 26.dp

private val CurrentTitleHorizontalSpacing = HorizontalPadding
    .plus(ActionButtonSize.times(3))
    .plus(ActionButtonsSpacing.times(3))

private val PreviousTitleWidth = CurrentTitleHorizontalSpacing
    .minus(HorizontalPadding.plus(BackIconWidth).plus(ActionButtonsSpacing))

@Composable
fun NavigationRoot(destination: Destination<Unit>) {
    CompositionLocalProvider(LocalNavigationManager provides NavigationManager("Root", destination)) {
        BoxWithConstraints {
            Column {
                val maxWidth = this@BoxWithConstraints.constraints.maxWidth
                val navManager = LocalNavigationManager.current
                AnimatedVisibility(navManager.currentScreen?.destination?.title != null) {
                    NavigationBar(maxWidth = maxWidth)
                }
                Box {
                    ScreenStack(maxWidth = maxWidth)
                    DragHandle(maxWidth = maxWidth)
                }
            }
        }
        val navManager = LocalNavigationManager.current
        UIControllerLifecycleEffect(UIControllerEvent.ViewWillDisappear) {
            navManager.currentScreen?.onStop()
        }
    }
}

@Composable
private fun NavigationBar(maxWidth: Int) {
    Box(contentAlignment = Alignment.CenterStart,
        modifier = Modifier.padding(top = 6.dp, bottom = 4.dp).height(32.dp)
    ) {
        val titleStyle = MaterialTheme.typography.titleMedium.copy(
            fontSize = 16.sp.unsalable(LocalDensity.current),
            color = MaterialTheme.colorScheme.onBackground
        )
        CurrentScreenTitle(
            maxWidth = maxWidth,
            style = titleStyle
        )
        PreviousScreenTitle(
            maxWidth = maxWidth,
            style = titleStyle
        )
        StartActions()
        EndActions(maxWidth = maxWidth)
    }
}

@Composable
private fun CurrentScreenTitle(
    maxWidth: Int,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        val textMeasurer = rememberTextMeasurer()
        val navManager = LocalNavigationManager.current
        val density = LocalDensity.current
        val currentScreen = navManager.currentScreen
        if (currentScreen != null) {
            val titleWidth = remember(density, textMeasurer, style, currentScreen.destination.title) {
                textMeasurer.measure(
                    text = currentScreen.destination.title.orEmpty(),
                    style = style
                ).size.width.coerceAtMost(
                    maxWidth - CurrentTitleHorizontalSpacing.times(2).toPx(density).roundToInt()
                )
            }
            val endPadding = titleWidth + HorizontalPadding.toPx(density)
            Text(
                text = currentScreen.destination.title.orEmpty(),
                style = style,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.offset {
                    val centeringOffset = maxWidth.div(2).minus(titleWidth.div(2))
                    IntOffset(
                        y = 0,
                        x = centeringOffset.plus(navManager.topScreenOffsetRatio.value.times(
                            maxWidth - (centeringOffset + endPadding)
                        )).toInt()
                    )
                }.width(titleWidth.toDp(density))
                    .alpha(1f.minus(navManager.topScreenOffsetRatio.value))
            )
        }
    }
}

@Suppress("UnusedReceiverParameter")
@Composable
private fun BoxScope.PreviousScreenTitle(
    maxWidth: Int,
    style: TextStyle,
) {
    val textMeasurer = rememberTextMeasurer()
    val scope = rememberCoroutineScope()
    val navManager = LocalNavigationManager.current
    val density = LocalDensity.current
    val currentScreen = navManager.currentScreen
    val prevScreen = navManager.prevScreen
    if (prevScreen != null) {
        val backIconAlpha = when {
            currentScreen?.destination?.barActions?.start.orEmpty().isNotEmpty() -> {
                if (prevScreen.destination.barActions.start.isNotEmpty()) {
                    0f
                } else {
                    navManager.topScreenOffsetRatio.value
                }
            }
            prevScreen.destination.barActions.start.isNotEmpty() -> {
                1f - navManager.topScreenOffsetRatio.value
            }
            navManager.backstack.size == 2 -> 1f - navManager.topScreenOffsetRatio.value
            else -> 1f
        }
        Image(
            imageVector = Icons.Rounded.KeyboardArrowLeft,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.clickable(
                enabled = Showcase.acceptsInputs && backIconAlpha == 1f,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                scope.launch {
                    navManager.onBack()
                }
            }.padding(horizontal = HorizontalPadding)
                .size(width = BackIconWidth, height = 32.dp)
                .mirror(LocalLayoutDirection.current)
                .alpha(backIconAlpha)
        )
        val titleAlpha = when {
            currentScreen?.destination?.barActions?.start.orEmpty().isNotEmpty() -> {
                if (prevScreen.destination.barActions.start.isNotEmpty()) {
                    0f
                } else {
                    navManager.topScreenOffsetRatio.value
                }
            }
            prevScreen.destination.barActions.start.isNotEmpty() -> {
                1f - navManager.topScreenOffsetRatio.value
            }
//            navManager.backstack.size == 2 -> 1f - navManager.topScreenOffsetRatio.value
            else -> 1f
        }
        val titleWidth = remember(density, textMeasurer, style, prevScreen.destination.title) {
            textMeasurer.measure(
                text = prevScreen.destination.title.orEmpty(),
                style = style
            ).size.width.coerceAtMost(PreviousTitleWidth.toPx(density).roundToInt())
        }
        val leftIconWidth = HorizontalPadding.plus(BackIconWidth).toPx(density)
        Text(
            text = prevScreen.destination.title.orEmpty(),
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.offset {
                val centeringOffset = maxWidth.div(2).minus(titleWidth.div(2))
                IntOffset(
                    y = 0,
                    x = centeringOffset.minus(
                        1f.minus(navManager.topScreenOffsetRatio.value)
                            .times(centeringOffset - leftIconWidth)
                    ).toInt()
                )
            }.width(titleWidth.toDp(density)).clickable(
                enabled = Showcase.acceptsInputs,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                scope.launch {
                    navManager.onBack()
                }
            }
        )
    }
}

@Suppress("UnusedReceiverParameter")
@Composable
private fun BoxScope.EndActions(maxWidth: Int) {
    val density = LocalDensity.current
    val navManager = LocalNavigationManager.current
    val currentActions = navManager.currentScreen?.destination?.barActions?.end.orEmpty()
    NavBarActions(
        actions = { currentActions },
        modifier = Modifier.offset {
            IntOffset(
                y = 0,
                x = maxWidth.minus(currentActions.size.times(ActionButtonSize.toPx(density)) +
                        currentActions.size.minus(1)
                            .coerceAtLeast(0)
                            .times(ActionButtonsSpacing.toPx(density))
                            .plus(HorizontalPadding.toPx(density))
                ).roundToInt()
            )
        }.alpha(1f.minus(navManager.topScreenOffsetRatio.value))
    )
    if (navManager.topScreenOffsetRatio.value > 0f) {
        val prevActions = navManager.prevScreen?.destination?.barActions?.end.orEmpty()
        NavBarActions(
            actions = { prevActions },
            modifier = Modifier.offset {
                IntOffset(
                    y = 0,
                    x = maxWidth.minus(prevActions.size.times(ActionButtonSize.toPx(density)) +
                            prevActions.size.minus(1)
                                .coerceAtLeast(0)
                                .times(ActionButtonsSpacing.toPx(density))
                                .plus(HorizontalPadding.toPx(density))
                    ).roundToInt()
                )
            }.alpha(navManager.topScreenOffsetRatio.value)
        )
    }
}

@Suppress("UnusedReceiverParameter")
@Composable
private fun BoxScope.StartActions() {
    val density = LocalDensity.current
    val navManager = LocalNavigationManager.current
    val currentActions = navManager.currentScreen?.destination?.barActions?.start.orEmpty()
    if (currentActions.isNotEmpty()) {
        NavBarActions(
            actions = { currentActions },
            modifier = Modifier.offset {
                IntOffset(
                    y = 0,
                    x = HorizontalPadding.toPx(density).roundToInt()
                )
            }.alpha(1f.minus(navManager.topScreenOffsetRatio.value))
        )
    }
    val prevActions = navManager.prevScreen?.destination?.barActions?.start.orEmpty()
    if (navManager.topScreenOffsetRatio.value > 0f && prevActions.isNotEmpty()) {
        NavBarActions(
            actions = { prevActions },
            modifier = Modifier.offset {
                IntOffset(
                    y = 0,
                    x = HorizontalPadding.toPx(density).roundToInt()
                )
            }.alpha(navManager.topScreenOffsetRatio.value)
        )
    }
}

@Composable
private fun NavBarActions(
    actions: () -> List<ActionButton>,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(ActionButtonsSpacing),
        modifier = modifier
    ) {
        actions().fastForEach { action ->
            ActionButton(
                imageVector = action.icon(),
                onClick = action.onClick,
                enabled = action.enabled(),
                contentPadding = PaddingValues(4.dp),
                colorFilter = action.colorFilter(),
                modifier = Modifier.size(ActionButtonSize)
            )
        }
    }
}

@Composable
private fun ScreenStack(maxWidth: Int) {
    val navManager = LocalNavigationManager.current
    navManager.backstack.fastForEachIndexed { index, entry ->
        key(entry.key) {
            Box(Modifier.offset {
                when (index) {
                    navManager.backstack.lastIndex -> IntOffset(
                        y = 0,
                        x = navManager.topScreenOffsetRatio.value.times(maxWidth).roundToInt()
                    )
                    navManager.backstack.lastIndex - 1 -> IntOffset(
                        y = 0,
                        x = maxWidth.toFloat()
                            .minus(navManager.topScreenOffsetRatio.value.times(maxWidth))
                            .times(0.25f)
                            .roundToInt()
                            .unaryMinus()
                    )
                    else -> IntOffset.Zero
                }
            }, content = {
                entry.content()
            })
        }
    }
}

@Composable
private fun DragHandle(maxWidth: Int) {
    val scope = rememberCoroutineScope()
    val navManager = LocalNavigationManager.current
    var blocker by remember { mutableStateOf<InputBlocker?>(null) }
    val hasActiveHandlers = navManager.currentScreen?.destination?.backHandlers.orEmpty().any {
        it.enabled
    }
    Box(modifier = Modifier
        .fillMaxHeight()
        .width(HorizontalPadding)
        .draggable(
            state = rememberDraggableState { delta ->
                if (!hasActiveHandlers) {
                    scope.launch {
                        navManager.topScreenOffsetRatio.snapTo(navManager.topScreenOffsetRatio.value
                            .plus(delta.div(maxWidth))
                            .coerceAtLeast(0f)
                        )
                    }
                    navManager.handleDragDirection = if (delta > 0) {
                        DragDirection.End
                    } else {
                        DragDirection.Start
                    }
                }
            }, orientation = Orientation.Horizontal,
            enabled = navManager.backstack.size > 1 && Showcase.acceptsInputs(excluding = setOfNotNull(blocker)),
            onDragStarted = {
                blocker = Showcase.block()
                if (hasActiveHandlers) {
                    navManager.triggerBackHandlers()
                }
            }, onDragStopped = {
                val shouldPop = navManager.topScreenOffsetRatio.value >= 0.5f ||
                        (navManager.handleDragDirection == DragDirection.End && navManager.topScreenOffsetRatio.value >= 0.2f)
                if (shouldPop) {
                    scope.launch {
                        navManager.pop(null)
                    }
                } else {
                    navManager.topScreenOffsetRatio.animateTo(0f)
                }
                blocker?.let(Showcase::unblock)
            }
        )
    )
}
