package dev.ahmedmourad.showcase.common.pickers.time

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.get
import dev.ahmedmourad.showcase.common.pickers.desc
import dev.ahmedmourad.showcase.common.utils.format
import dev.ahmedmourad.showcase.common.pickers.toDp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimePicker(
    value: () -> LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val isAM = remember(value()) {
            value().hour < 12
        }
        val style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
        val hoursState = rememberPickerState()
        val minutesState = rememberPickerState()
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Row(Modifier.weight(2f)) {
                WheelPicker(
                    items = (1..12).toList().map { it.format() },
                    cyclic = true,
                    textStyle = style,
                    startIndex = if (isAM) {
                        if (value().hour == 0) 11 else value().hour - 1
                    } else {
                        if (value().hour == 12) 11 else value().hour - 13
                    }, state = hoursState,
                    enabled = Showcase.acceptsInputs,
                    modifier = Modifier.weight(1f)
                )
                WheelPicker(
                    items = (0..59).toList().map { it.format().padStart(2, 0.format().first()) },
                    cyclic = true,
                    textStyle = style,
                    state = minutesState,
                    startIndex = value().minute,
                    enabled = Showcase.acceptsInputs,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        val amPmState = rememberPickerState()
        val amPmItems = listOf(RR.strings.am.desc().get(), RR.strings.pm.desc().get())
        WheelPicker(
            items = amPmItems,
            cyclic = true,
            textStyle = style,
            startIndex = if (isAM) 0 else 1,
            state = amPmState,
            enabled = Showcase.acceptsInputs,
            modifier = Modifier.weight(1f)
        )
        LaunchedEffect(hoursState.selectedItem, minutesState.selectedItem, amPmState.selectedItem) {
            val hourInt = hoursState.selectedItem.toInt()
            onValueChange(LocalTime(
                hour =  if (amPmState.selectedItem == amPmItems.first()) {
                    if (hourInt == 12) 0 else hourInt
                } else {
                    if (hourInt == 12) 12 else hourInt + 12
                }, minute = minutesState.selectedItem.toInt(),
                second = 0,
                nanosecond = 0
            ))
        }
    }
}

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalTextApi::class)
@Composable
fun WheelPicker(
    items: List<String>,
    state: PickerState,
    cyclic: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    textStyle: TextStyle = LocalTextStyle.current
) {
    val density = LocalDensity.current
    val visibleItemsCount = 3
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = if (cyclic) Int.MAX_VALUE else items.size
    val listScrollMiddle = listScrollCount / 2

    fun getItem(index: Int) = items[index % items.size]
    fun cyclicIndex(index: Int) = listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + index

    val listStartIndex = if (cyclic) cyclicIndex(startIndex) else startIndex
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val textMeasurer = rememberTextMeasurer()
    val itemHeightDp = remember(items, textMeasurer) {
        textMeasurer.measure(items.random(), textStyle).size.height.toDp(density) + 24.dp
    }

    val colors = MaterialTheme.colorScheme
    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to colors.background,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(if (cyclic) index + visibleItemsMiddle else index) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {
        val scope = rememberCoroutineScope()
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = enabled,
            modifier = Modifier
                .height(itemHeightDp * visibleItemsCount)
                .fillMaxWidth()
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.height(itemHeightDp)
                ) {
                    Text(
                        text = getItem(index),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = textStyle,
                        modifier = Modifier.clickable(
                            enabled = enabled,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch { listState.animateScrollToItem(index - 1) }
                        }
                    )
                }
            }
        }
        Divider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle)
        )
        Divider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1))
        )
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }
