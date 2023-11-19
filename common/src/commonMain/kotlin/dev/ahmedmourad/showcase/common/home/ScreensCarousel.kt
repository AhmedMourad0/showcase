package dev.ahmedmourad.showcase.common.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import androidx.compose.ui.unit.lerp
import dev.ahmedmourad.showcase.common.pickers.toDp
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.blocker
import dev.ahmedmourad.showcase.common.compose.components.ActionButton
import dev.ahmedmourad.showcase.common.compose.components.ThemeModeActionButton
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreensCarousel(
    state: CarouselState,
    onStateChange: (CarouselState) -> Unit,
    list: () -> List<CarouselScreen>,
    modifier: Modifier = Modifier,
    autoScrollDuration: Long = 4000L
) {
    Box(modifier) {
        val screens = list()
        val startIndex = Int.MAX_VALUE / 2
        val pagerState: PagerState = rememberPagerState(pageCount = { Int.MAX_VALUE }, initialPage = startIndex)
        val transition = updateTransition(state, "expansionTransition")
        val ratio by transition.animateFloat(
            transitionSpec = { tween(durationMillis = ExpansionDuration) },
            label = "ratio"
        ) {
            when (it) {
                CarouselState.Collapsed -> 1f
                CarouselState.Expanded -> 0f
            }
        }
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 28.dp * ratio, vertical = 90.dp * ratio),
            pageSpacing = 16.dp * ratio,
            userScrollEnabled = state == CarouselState.Collapsed
        ) { index ->
            val shape = RoundedCornerShape(8.dp * ratio)
            Column(Modifier
                .fillMaxSize()
                .carouselTransition(index, pagerState)
                .shadow(8.dp * ratio, shape)
                .clip(shape)
                .background(lerp(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                    MaterialTheme.colorScheme.background,
                    1f - ratio
                ), shape).clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = Showcase.acceptsInputs && state == CarouselState.Collapsed,
                    onClick = { onStateChange(CarouselState.Expanded) }
                )
            ) {
                val density = LocalDensity.current
                val item = screens[(index - startIndex).mod(screens.size)]
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
                    start = HorizontalPadding,
                    end = HorizontalPadding,
                    top = lerp(8.dp, WindowInsets.systemBars.getTop(density).toDp(density) + 16.dp, 1f - ratio),
                    bottom = lerp(8.dp, 16.dp, 1f - ratio)
                ).fillMaxWidth()) {
                    AnimatedVisibility(
                        visible = state == CarouselState.Expanded,
                        enter = fadeIn(tween(ExpansionDuration)) + expandVertically(tween(ExpansionDuration)),
                        exit = fadeOut(tween(ExpansionDuration)) + shrinkVertically(tween(ExpansionDuration))
                    ) {
                        ActionButton(
                            imageVector = Icons.Rounded.Close,
                            onClick = {
                                onStateChange(CarouselState.Collapsed)
                            }, enabled = state == CarouselState.Expanded
                        )
                    }
                    Text(
                        text = item.title,
                        color = Color.White,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 4.dp).weight(1f)
                    )
                    AnimatedVisibility(
                        visible = state == CarouselState.Expanded,
                        enter = fadeIn(tween(ExpansionDuration)) + expandVertically(tween(ExpansionDuration)),
                        exit = fadeOut(tween(ExpansionDuration)) + shrinkVertically(tween(ExpansionDuration))
                    ) {
                        ThemeModeActionButton(enabled = state == CarouselState.Expanded)
                    }
                }
                item.content()
            }
        }
        val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
        if (isDragged.not() && state == CarouselState.Collapsed) {
            if (screens.isNotEmpty()) {
                var currentPageKey by remember { mutableIntStateOf(0) }
                LaunchedEffect(currentPageKey) {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (pagerState.currentPage + 1).mod(Int.MAX_VALUE)
                    Showcase.blocker {
                        pagerState.animateScrollToPage(
                            page = nextPage,
                            animationSpec = tween(durationMillis = 400)
                        )
                    }
                    currentPageKey = nextPage
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.carouselTransition(
    index: Int,
    pagerState: PagerState
) = graphicsLayer {
    val pageOffset = pagerState.currentPage
        .minus(index)
        .plus(pagerState.currentPageOffsetFraction)
        .absoluteValue
    val ratio = lerp(
        start = 0.8f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(
            0f,
            1f
        )
    )
    alpha = ratio
    scaleY = ratio
}

/**
* Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
*/
fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}
