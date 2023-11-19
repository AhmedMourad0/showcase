package dev.ahmedmourad.showcase.common.pickers.time

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.pickers.clearSecondsAndNanos
import dev.ahmedmourad.showcase.common.utils.format
import dev.ahmedmourad.showcase.common.pickers.padAt
import dev.ahmedmourad.showcase.common.pickers.removeAt
import dev.ahmedmourad.showcase.common.pickers.unsalable
import dev.ahmedmourad.showcase.common.pickers.withHour
import dev.ahmedmourad.showcase.common.pickers.withMinute
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextTimePicker(
    value: () -> LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(IntrinsicSize.Max)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val isAM = remember(value()) {
            value().hour < 12
        }
        val focusRequester = remember { FocusRequester() }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Row(Modifier.weight(2f)) {
                TimeTextField(
                    value = if (isAM) {
                        if (value().hour == 0) 12 else value().hour
                    } else {
                        if (value().hour == 12) 12 else value().hour - 12
                    }, onValueChange = {
                        onValueChange(value().withHour(if (isAM) {
                            if (it == 12) 0 else it
                        } else {
                            if (it == 12) 12 else it + 12
                        }).clearSecondsAndNanos())
                    }, range = { 1..12 },
                    enabled = Showcase.acceptsInputs,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions.Default,
                    modifier = Modifier.weight(1f).focusRequester(focusRequester)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = ":",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier
                )
                Spacer(Modifier.width(8.dp))
                TimeTextField(
                    value = value().minute,
                    onValueChange = {
                        onValueChange(value().withMinute(it).clearSecondsAndNanos())
                    },
                    range = { 0..59 },
                    enabled = Showcase.acceptsInputs,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    onBack = { focusRequester.requestFocus() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(
            Modifier
                .width(IntrinsicSize.Min)
                .fillMaxHeight()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(
                        color = if (isAM) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                        }, shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    ).clickable(enabled = Showcase.acceptsInputs) {
                        if (!isAM) {
                            onValueChange(value().withHour(value().hour - 12))
                        }
                    }.padding(horizontal = 12.dp).fillMaxWidth()
            ) {
                Text(
                    text = stringResource(RR.strings.am),
                    style = MaterialTheme.typography.bodyLarge.copy(color = if (isAM) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    })
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(
                        color = if (isAM) {
                            MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                        } else {
                            MaterialTheme.colorScheme.primary
                        }, shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    ).clickable(enabled = Showcase.acceptsInputs) {
                        if (isAM) {
                            onValueChange(value().withHour(value().hour + 12))
                        }
                    }.padding(horizontal = 12.dp).fillMaxWidth()
            ) {
                Text(
                    text = stringResource(RR.strings.pm),
                    style = MaterialTheme.typography.bodyLarge.copy(color = if (isAM) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    })
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun TimeTextField(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: () -> IntRange,
    enabled: Boolean,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { }
) {
    val maxLength = remember(range()) { range().last.toString().length }
    var hasFocus by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value.format().padStart(maxLength, 0.format().first()))) }
    var prevTextFieldValue by remember { mutableStateOf(textFieldValue) }
    LaunchedEffect(value) {
        textFieldValue = textFieldValue.copy(text = value.format().padStart(maxLength, 0.format().first()))
        prevTextFieldValue = textFieldValue
    }
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction, autoCorrect = false),
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        enabled = enabled,
        maxLines = 1,
        textStyle = TextStyle(
            fontSize = 40.sp.unsalable(LocalDensity.current),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        ), colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            containerColor = if (hasFocus) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            } else {
                MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
            }
        ), value = textFieldValue,
        onValueChange = { new ->
            val newValue = when  {
                new.text.length == maxLength -> new
                new.text.length < maxLength -> new.copy(
                    text = new.text.padAt(maxLength, new.selection.start, 0.format().first())
                )
                new.text.length - maxLength == 1 -> new.copy(
                    text = new.text.removeAt(new.selection.end.coerceAtMost(new.text.lastIndex)),
                    selection = TextRange(new.selection.end.coerceAtMost(maxLength))
                )
                else -> new.copy(
                    text = new.text.substring(0, maxLength),
                    selection = TextRange(maxLength)
                )
            }
            newValue.text
                .toIntOrNull()
                ?.coerceIn(range())
                ?.let {
                    if (new.text == textFieldValue.text) {
                        textFieldValue = newValue.copy(text = it.format().padStart(maxLength, 0.format().first()))
                        prevTextFieldValue = textFieldValue
                    } else {
                        prevTextFieldValue = textFieldValue
                        textFieldValue = newValue.copy(text = it.format().padStart(maxLength, 0.format().first()))
                    }
                    onValueChange(it)
                }
        }, modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .onFocusEvent {
                hasFocus = it.isFocused
            }.onKeyEvent { event ->
                if (event.key == Key.Backspace) {
                    if (prevTextFieldValue == textFieldValue && textFieldValue.selection.length == 0 && textFieldValue.selection.start == 0) {
                        onBack()
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
    )
}
