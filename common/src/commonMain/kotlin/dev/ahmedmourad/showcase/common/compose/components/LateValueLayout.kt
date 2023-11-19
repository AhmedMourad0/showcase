package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.Parcelable
import dev.ahmedmourad.showcase.common.utils.*

@Composable
fun <E : Parcelable?, T : Parcelable?> LateListLayout(
    value: () -> LateList<E, T>,
    modifier: Modifier = Modifier,
    errorContent: @Composable (E) -> Unit = { UnicodeError() },
    loadingContent: @Composable () -> Unit,
    emptyContent: @Composable () -> Unit,
    successContent: @Composable (List<T>) -> Unit
) {
    Box(modifier) {
        when (val v = value()) {
            Loading -> key("Loading") {
                loadingContent()
            }
            is Error ->  key("Error") {
                errorContent(v.e)
            }
            is LateList.Success -> {
                if (v.v.isEmpty()) {
                    key("Empty") {
                        emptyContent()
                    }
                } else {
                    key("success") {
                        successContent(v.v)
                    }
                }
            }
        }
    }
}

@Composable
fun <E : Parcelable?, T : Parcelable?> LateValueLayout(
    value: () -> LateValue<E, T>,
    modifier: Modifier = Modifier,
    errorContent: @Composable (E) -> Unit = { UnicodeError() },
    loadingContent: @Composable () -> Unit,
    successContent: @Composable (T) -> Unit
) {
    Box(modifier) {
        when (val v = value()) {
            Loading -> key("Loading") {
                loadingContent()
            }
            is Error -> key("Error") {
                errorContent(v.e)
            }
            is LateValue.Success -> key("Success") {
                successContent(v.v)
            }
        }
    }
}

@Composable
fun BasicLoading(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        CircularProgressIndicator(Modifier.fillMaxWidth(0.12f))
    }
}

@Composable
fun UnicodeLoading(text: String, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val scaleX by transition.animateFloat(
        initialValue = 1f,
        targetValue = -1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    UnicodePlaceholder(
        unicode = "༼ つ ◕_◕ ༽つ",
        text = text,
        unicodeScaleX = if (scaleX > 0) 1f else -1f,
        modifier = modifier
    )
}

@Composable
fun UnicodeError(modifier: Modifier = Modifier) {
    UnicodePlaceholder(
        unicode = "༼ つ . _ . ༽つ",
        text = somethingWentWrong().v,
        modifier = modifier
    )
}

@Composable
fun SadUnicodeEmpty(text: String, modifier: Modifier = Modifier) {
    UnicodePlaceholder(
        unicode = "༼ つ ; _ ; ༽つ",
        text = text,
        modifier = modifier
    )
}

@Composable
fun HappyUnicodeEmpty(text: String, modifier: Modifier = Modifier) {
    UnicodePlaceholder(
        unicode = "༼ つ ◕‿◕ ༽つ",
        text = text,
        modifier = modifier
    )
}

@Composable
private fun UnicodePlaceholder(
    unicode: String,
    text: String,
    modifier: Modifier = Modifier,
    unicodeScaleX: Float = 1f
) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        @Suppress("NAME_SHADOWING")
        val unicodeScaleX by rememberUpdatedState(unicodeScaleX)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(
                text = unicode,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ), modifier = Modifier.graphicsLayer {
                    this.scaleX = unicodeScaleX
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}
