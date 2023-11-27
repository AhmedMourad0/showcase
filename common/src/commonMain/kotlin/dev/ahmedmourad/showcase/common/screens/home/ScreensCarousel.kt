package dev.ahmedmourad.showcase.common.screens.home

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.components.ActionButton
import dev.ahmedmourad.showcase.common.compose.components.ThemeModeActionButton
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import kotlin.math.absoluteValue

private const val ExpansionDuration = 400

@Immutable
enum class CarouselState {
    Collapsed, Expanded
}

@Immutable
data class CarouselScreen(
    val title: String,
    val content: @Composable () -> Unit
)

@Composable
fun ScreensCarousel(
    state: () -> CarouselState,
    onStateChange: (CarouselState) -> Unit,
    screens: () -> List<CarouselScreen>,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))) {
        val transition = updateTransition(state(), "expansionTransition")
        val expansionRatio by transition.animateFloat(
            transitionSpec = { tween(durationMillis = ExpansionDuration) },
            label = "ratio"
        ) { state ->
            when (state) {
                CarouselState.Collapsed -> 0f
                CarouselState.Expanded -> 1f
            }
        }
        CarouselPager(
            screens = screens,
            state = state,
            onStateChange = onStateChange,
            expansionRatio = { expansionRatio }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarouselPager(
    screens: () -> List<CarouselScreen>,
    state: () -> CarouselState,
    onStateChange: (CarouselState) -> Unit,
    expansionRatio: () -> Float,
    modifier: Modifier = Modifier
) {
    val startIndex = Int.MAX_VALUE / 2
    @Suppress("NAME_SHADOWING")
    val screens = screens()
    val pagerState = rememberPagerState(
        pageCount = { Int.MAX_VALUE },
        initialPage = startIndex
    )
    val layoutDirection = LocalLayoutDirection.current
    val safeDrawingPadding = WindowInsets.safeDrawing.asPaddingValues()
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(
            start = lerp(
                28.dp + safeDrawingPadding.calculateStartPadding(layoutDirection),
                0.dp,
                expansionRatio()
            ), end = lerp(
                28.dp + safeDrawingPadding.calculateEndPadding(layoutDirection),
                0.dp,
                expansionRatio()
            ), top = lerp(
                60.dp + safeDrawingPadding.calculateTopPadding(),
                0.dp,
                expansionRatio()
            ), bottom = lerp(
              60.dp + safeDrawingPadding.calculateBottomPadding(),
                0.dp,
                expansionRatio()
            )
        ), pageSpacing = lerp(16.dp, 0.dp, expansionRatio()),
        userScrollEnabled = state() == CarouselState.Collapsed,
        key = { calculateScreenIndex(it, startIndex, screens.size) },
        modifier = modifier
    ) { index ->
        CarouselScreen(
            value = { screens[calculateScreenIndex(index, startIndex, screens.size)] },
            state = state,
            onStateChange = onStateChange,
            expansionRatio = expansionRatio,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Stable
private fun calculateScreenIndex(index: Int, startIndex: Int, size: Int): Int {
    return (index - startIndex).mod(size)
}

@Composable
private fun CarouselScreen(
    value: () -> CarouselScreen,
    state: () -> CarouselState,
    onStateChange: (CarouselState) -> Unit,
    expansionRatio: () -> Float,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(lerp(8.dp, 0.dp, expansionRatio()))
    val layoutDirection = LocalLayoutDirection.current
    val safeDrawingPadding = WindowInsets.safeDrawing.asPaddingValues()
    Box(modifier
        .fillMaxSize()
        .padding(
            start = lerp(
                0.dp,
                safeDrawingPadding.calculateStartPadding(layoutDirection),
                expansionRatio()
            ), end = lerp(
                0.dp,
                safeDrawingPadding.calculateEndPadding(layoutDirection),
                expansionRatio()
            ), top = lerp(
                0.dp,
                safeDrawingPadding.calculateTopPadding(),
                expansionRatio()
            ), bottom = lerp(
                0.dp,
                safeDrawingPadding.calculateBottomPadding(),
                expansionRatio()
            )
        ).graphicsLayer {
            this.shadowElevation = lerp(8.dp.toPx(), 0f, expansionRatio())
            this.shape = shape
        }.background(MaterialTheme.colorScheme.background, shape)
    ) {
        Column(Modifier.fillMaxSize()) {
            ScreenTopBar(
                screen = value,
                state = state,
                onStateChange = onStateChange,
                expansionRatio = expansionRatio
            )
            Box(Modifier.weight(1f)) {
                value().content()
            }
        }
        if (state() == CarouselState.Collapsed) {
            Box(Modifier.fillMaxSize().clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = Showcase.acceptsInputs,
                onClick = { onStateChange(CarouselState.Expanded) }
            ))
        }
    }
}

@Composable
private fun ScreenTopBar(
    screen: () -> CarouselScreen,
    state: () -> CarouselState,
    onStateChange: (CarouselState) -> Unit,
    expansionRatio: () -> Float,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(
            start = HorizontalPadding,
            end = HorizontalPadding,
            top = lerp(8.dp, 12.dp, expansionRatio()),
            bottom = lerp(8.dp, 12.dp, expansionRatio())
        )
    ) {
        ActionButton(
            imageVector = Icons.Rounded.Close,
            onClick = { onStateChange(CarouselState.Collapsed) },
            enabled = state() == CarouselState.Expanded,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.graphicsLayer {
                scaleY = expansionRatio()
                alpha = expansionRatio()
            }.size(40.dp)
        )
        Text(
            text = screen().title,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 4.dp).weight(1f)
        )
        ThemeModeActionButton(
            enabled = state() == CarouselState.Expanded,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.graphicsLayer {
                scaleY = expansionRatio()
                alpha = expansionRatio()
            }.size(40.dp)
        )
    }
}

/**
* Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
*/
@Stable
fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}
