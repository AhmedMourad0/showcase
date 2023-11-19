package dev.ahmedmourad.showcase.common.pickers.time

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.modifiers.mirror
import dev.ahmedmourad.showcase.common.compose.theme.GeneralCornerRadius
import dev.ahmedmourad.showcase.common.utils.format
import dev.ahmedmourad.showcase.common.pickers.replaceAt
import dev.ahmedmourad.showcase.common.pickers.withHour
import dev.ahmedmourad.showcase.common.pickers.withMinute
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.datetime.LocalTime

@Composable
fun KeyboardTimePicker(
    value: () -> LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(contentPadding)
    ) {
        var focusedIndex by remember { mutableStateOf(0) }
        TimeIndicator(
            time = value,
            focusedIndex = focusedIndex
        )
        Spacer(modifier = Modifier.height(8.dp))
        TimeKeyboard(
            onDigit = { digit ->
                onValueChange(when (val index = focusedIndex.coerceIn(0, 3)) {
                    0, 1 -> value().withHour(value().hour.format().padStart(2, 0.format().first()).replaceAt(index, digit.format()).toInt())
                    2, 3 -> value().withMinute(value().minute.format().padStart(2, 0.format().first()).replaceAt(index - 2, digit.format()).toInt())
                    else -> throw IllegalArgumentException("This should never trigger")
                })
                focusedIndex = (focusedIndex + 1) % 4
            }, onNext = { focusedIndex = (focusedIndex + 1) % 4 },
            onPrevious = { focusedIndex = (focusedIndex + 4 - 1) % 4 }
        )
    }
}

@Composable
private fun TimeKeyboard(
    onDigit: (Int) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = 8.dp
    val buttonSize = 50.dp
    Column(verticalArrangement = Arrangement.spacedBy(spacing), modifier = modifier) {
        val chunks = (1..9).chunked(3)
        chunks.forEach { chunk ->
            key(chunk.first()) {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                    chunk.forEach { digit ->
                        key(digit) {
                            KeyboardButton(
                                onClick = { onDigit.invoke(digit) },
                                modifier = Modifier.size(buttonSize)
                            ) {
                                KeyboardButtonText(digit.format())
                            }
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            val layoutDirection = LocalLayoutDirection.current
            KeyboardButton(
                onClick = onPrevious,
                background = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(buttonSize)
            ) {
                KeyboardButtonIcon(
                    icon = { painterResource(RR.images.arrow_left) },
                    color = MaterialTheme.colorScheme.onSecondary,
                    contentDescription = "Move left",
                    modifier = Modifier.mirror(layoutDirection)
                )
            }
            KeyboardButton(
                onClick = { onDigit.invoke(0) },
                modifier = Modifier.size(buttonSize)
            ) {
                KeyboardButtonText(0.format())
            }
            KeyboardButton(
                onClick = onNext,
                background = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(buttonSize)
            ) {
                KeyboardButtonIcon(
                    icon = { painterResource(RR.images.arrow_right) },
                    color = MaterialTheme.colorScheme.onSecondary,
                    contentDescription = "Move right",
                    modifier = Modifier.mirror(layoutDirection)
                )
            }
        }
    }
}

@Composable
private fun KeyboardButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.primary,
    content: @Composable BoxScope.() -> Unit
) {
    Box(content = content,
        modifier = modifier
            .clip(CircleShape)
            .background(background, CircleShape)
            .clickable(enabled = Showcase.acceptsInputs, onClick = onClick)
    )
}

@Composable
private fun BoxScope.KeyboardButtonText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        ), modifier = modifier.align(Alignment.Center)
    )
}

@Composable
private fun BoxScope.KeyboardButtonIcon(
    icon: @Composable () -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    Image(
        painter = icon.invoke(),
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier.size(20.dp).align(Alignment.Center)
    )
}

@Composable
private fun TimeIndicator(
    time: () -> LocalTime,
    focusedIndex: Int?,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            TimeDisplay(
                value = time().hour,
                focusedIndex = focusedIndex?.takeIf { it < 2 }?.coerceIn(0, 1),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 50.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = ":",
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 50.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
            TimeDisplay(
                value = time().minute,
                focusedIndex = focusedIndex?.takeIf { it >= 2 }?.minus(2)?.coerceIn(0, 1),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 50.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun TimeDisplay(
    value: Int,
    focusedIndex: Int?,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val text = remember(colors.primary, value, focusedIndex) {
        buildTimeDisplayText(value, focusedIndex, colors.primary)
    }
    Text(
        text = text,
        style = style,
        maxLines = 1,
        modifier = modifier
            .clip(RoundedCornerShape(GeneralCornerRadius))
            .padding(4.dp)
    )
}

private fun buildTimeDisplayText(
    value: Int,
    focusedIndex: Int?,
    selectedColor: Color
): AnnotatedString = buildAnnotatedString {
    value.format()
        .padStart(2, 0.format().first())
        .forEachIndexed { index, char ->
            if (index == focusedIndex) {
                pushStyle(SpanStyle(color = selectedColor))
            }
            append(char)
            if (index == focusedIndex) {
                pop()
            }
        }
}
