package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.modifiers.mirror
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalSpacing
import dev.ahmedmourad.showcase.common.pickers.date.DatePickerDialog
import dev.ahmedmourad.showcase.common.pickers.date.rememberDatePickerState
import dev.ahmedmourad.showcase.common.pickers.time.TimePickerDialog
import dev.ahmedmourad.showcase.common.utils.Message
import dev.ahmedmourad.showcase.common.utils.format
import dev.ahmedmourad.showcase.common.pickers.withDate
import dev.ahmedmourad.showcase.common.pickers.withTime
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

@OptIn(ExperimentalMaterial3Api::class)
val OutlinedTextFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.outlinedTextFieldColors(
        MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

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
fun ShowcaseTextField(
    label: String,
    leadingIcon: (() -> ImageResource)?,
    keyboardOptions: KeyboardOptions,
    value: () -> String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    violations: List<Message> = emptyList(),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (() -> ImageResource)? = null,
    textStyle: TextStyle = LocalTextStyle.current
) {
    ShowcaseTextField(
        label = label,
        keyboardOptions = keyboardOptions,
        value = value,
        onValueChange = onValueChange,
        violations = violations,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        leadingIcon = icon(leadingIcon),
        trailingIcon = icon(trailingIcon),
        textStyle = textStyle,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseTextArea(
    label: String,
    keyboardOptions: KeyboardOptions,
    value: () -> String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    violations: List<Message> = emptyList(),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Validated(violations, modifier) { showError ->
        OutlinedTextField(
            isError = showError,
            label = { Text(label) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldColors,
            value = value.invoke(),
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            leadingIcon = {
                if (leadingIcon != null) {
                    Box(contentAlignment = Alignment.TopStart,
                        modifier = Modifier.padding(vertical = 16.dp).fillMaxHeight()
                    ) {
                        leadingIcon()
                    }
                }
            }, trailingIcon = trailingIcon,
            textStyle = textStyle,
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseTextField(
    label: String,
    keyboardOptions: KeyboardOptions,
    value: () -> String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    violations: List<Message> = emptyList(),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Validated(violations, modifier) { showError ->
        OutlinedTextField(
            isError = showError,
            label = { Text(label) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldColors,
            value = value.invoke(),
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TextDatePicker(
    value: () -> LocalDate,
    onValueChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(Showcase.acceptsInputs) {
                pickerState.displayed = value.invoke()
                showPicker = true
            }.padding(vertical = 12.dp)
    ) {
        Image(
            imageVector = Icons.Rounded.CalendarMonth,
            contentDescription = "Select date",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = value.invoke().format(),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(Modifier.width(8.dp))
        Image(
            imageVector = Icons.Rounded.KeyboardArrowDown,
            contentDescription = "Select date",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(20.dp)
        )
        DatePickerDialog(
            show = showPicker,
            selected = value,
            onSelectedChange = {
                onValueChange(it)
                showPicker = false
            }, state = pickerState,
            onDismissRequest = { showPicker = false },
        )
    }
}

@Composable
fun TextFieldDatePicker(
    value: () -> LocalDate,
    onValueChange: (LocalDate) -> Unit,
    hasTrailingIcon: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        var showPicker by remember { mutableStateOf(false) }
        PickerHeader(
            text = value.invoke().format(),
            maxLines = 1,
            onClick = { showPicker = true },
            leadingIcon = icon { Icons.Rounded.CalendarMonth },
            trailingIcon = icon(showIf = hasTrailingIcon) { Icons.Rounded.KeyboardArrowDown },
            modifier = Modifier.fillMaxWidth()
        )
        DatePickerDialog(
            show = showPicker,
            selected = value,
            onSelectedChange = {
                onValueChange(it)
                showPicker = false
            }, state = rememberDatePickerState(value.invoke()),
            onDismissRequest = { showPicker = false },
        )
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

@Composable
fun TextFieldDateTimePicker(
    value: () -> LocalDateTime,
    onValueChange: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        TextFieldDatePicker(
            value = { value.invoke().date },
            onValueChange = { onValueChange(value.invoke().withDate(it)) },
            hasTrailingIcon = false,
            modifier = Modifier.weight(6f)
        )
        Spacer(Modifier.width(HorizontalSpacing))
        TextFieldTimePicker(
            value = { value.invoke().time },
            onValueChange = { onValueChange(value.invoke().withTime(it)) },
            hasTrailingIcon = false,
            modifier = Modifier.weight(5f)
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

@OptIn(ExperimentalTextApi::class)
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
