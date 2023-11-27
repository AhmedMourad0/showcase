package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.modifiers.mirror
import dev.ahmedmourad.showcase.common.pickers.time.TimePickerDialog
import dev.ahmedmourad.showcase.common.utils.format
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.datetime.LocalTime
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

@JvmName("imageResourceIcon")
@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun icon(res: (() -> ImageResource)?): @Composable (() -> Unit)? {
    res ?: return null
    return {
        Image(
            painter = painterResource(res()),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
    }
}

@JvmName("imageVectorIcon")
@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun icon(modifier: Modifier = Modifier, showIf: Boolean = true, mirror: Boolean = false, res: (() -> ImageVector)?): @Composable (() -> Unit)? {
    res ?: return null
    return {
        if (showIf) {
            Image(
                imageVector = res(),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = modifier.then(if (mirror) {
                    Modifier.mirror(LocalLayoutDirection.current)
                } else {
                    Modifier
                })
            )
        }
    }
}

@Composable
fun TextFieldTimePicker(
    value: () -> LocalTime,
    onValueChange: (LocalTime) -> Unit,
    hasTrailingIcon: Boolean  = true,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        var showPicker by remember { mutableStateOf(false) }
        PickerHeader(
            text = value.invoke().format(),
            maxLines = 1,
            onClick = { showPicker = true },
            leadingIcon = icon { Icons.Rounded.AccessTime },
            trailingIcon = icon(showIf = hasTrailingIcon) { Icons.Rounded.KeyboardArrowDown },
            modifier = Modifier.fillMaxWidth()
        )
        TimePickerDialog(
            show = showPicker,
            value = value,
            onValueChange = { onValueChange(it) },
            onDismissRequest = { showPicker = false },
        )
    }
}

val PickerHeaderHorizontalSpacing = 12.dp

@Composable
fun PickerHeader(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    resizeToFit: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            ).clickable(onClick = onClick, enabled = Showcase.acceptsInputs)
            .padding(horizontal = PickerHeaderHorizontalSpacing, vertical = 16.dp)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(PickerHeaderHorizontalSpacing))
        }
        var textMaxWidth by remember { mutableStateOf(Int.MAX_VALUE) }
        Box(Modifier.weight(1f).onSizeChanged { textMaxWidth = it.width }) {
            val typography = MaterialTheme.typography
            val textStyle = if (maxLines > 1 || !resizeToFit) {
                typography.bodyLarge
            } else {
                rememberResizableTextStyle(
                    text = text,
                    style = typography.bodyLarge,
                    maxWidth = textMaxWidth
                )
            }
            Text(
                text = text,
                maxLines = maxLines,
                style = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (trailingIcon != null) {
            Spacer(Modifier.width(PickerHeaderHorizontalSpacing))
            trailingIcon()
        }
    }
}

@Stable
@Composable
fun rememberResizableTextStyle(text: String, style: TextStyle, maxWidth: Int): TextStyle {
    val textMeasurer = rememberTextMeasurer()
    return remember(textMeasurer, text, maxWidth, style) {
        generateSequence(style) { style ->
            val width = textMeasurer.measure(text = text, style = style).size.width
            if (width > maxWidth) {
                style.copy(fontSize = style.fontSize * 0.98f)
            } else {
                null
            }
        }.last()
    }
}
