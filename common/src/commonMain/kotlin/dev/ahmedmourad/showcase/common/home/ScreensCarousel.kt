package dev.ahmedmourad.showcase.common.home

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.components.ActionButton
import dev.ahmedmourad.showcase.common.compose.components.ThemeModeActionButton
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.pickers.toDp
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
    val currentPageOffsetFraction by remember(pagerState) {
        derivedStateOf {
            pagerState.currentPageOffsetFraction
        }
    }
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(
            horizontal = 28.dp * (1f - expansionRatio()),
            vertical = 60.dp * (1f - expansionRatio())
        ), pageSpacing = 16.dp * (1f - expansionRatio()),
        userScrollEnabled = state() == CarouselState.Collapsed,
        key = { calculateScreenIndex(it, startIndex, screens.size) },
        modifier = modifier
    ) { index ->
        CarouselScreen(
            value = { screens[calculateScreenIndex(index, startIndex, screens.size)] },
            state = state,
            onStateChange = onStateChange,
            expansionRatio = expansionRatio,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                val pageOffset = pagerState.currentPage
                    .minus(index)
                    .plus(currentPageOffsetFraction)
                    .absoluteValue
                scaleY = lerp(
                    start = 0.8f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }
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
    val shape = RoundedCornerShape(8.dp * (1f - expansionRatio()))
    Box(modifier
        .fillMaxSize()
        .graphicsLayer {
            this.shadowElevation = 8.dp.toPx() * (1f - expansionRatio())
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
    val density = LocalDensity.current
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(
            start = HorizontalPadding,
            end = HorizontalPadding,
            top = lerp(
                8.dp,
                WindowInsets.systemBars.getTop(density).toDp(density) + 12.dp,
                expansionRatio()
            ), bottom = lerp(8.dp, 12.dp, expansionRatio())
        ).fillMaxWidth()
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
