package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EventBusy
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.pickers.date.component.models.ShorterMonthFallback

@Composable
fun TextFieldShorterMonthFallbackPicker(
    value: () -> ShorterMonthFallback?,
    onValueChange: (ShorterMonthFallback) -> Unit,
    text: (ShorterMonthFallback?) -> String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        var showPicker by remember { mutableStateOf(false) }
        PickerHeader(
            text = text.invoke(value.invoke()),
            onClick = { showPicker = true },
            leadingIcon = icon { Icons.Rounded.EventBusy },
            trailingIcon = icon { Icons.Rounded.KeyboardArrowDown },
            modifier = Modifier.fillMaxWidth()
        )
        ShorterMonthFallbackPickerDialog(
            show = showPicker,
            value = value,
            text = text,
            onValueChange = {
                onValueChange(it)
                showPicker = false
            }, onDismissRequest = { showPicker = false }
        )
    }
}

@Composable
fun ShorterMonthFallbackPickerDialog(
    show: Boolean,
    value: () -> ShorterMonthFallback?,
    onValueChange: (ShorterMonthFallback) -> Unit,
    text: (ShorterMonthFallback) -> String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    OptionalDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        desktopType = ModelType.Popup(),
        mobileType = ModelType.DropdownMenu
    ) {
        Column(modifier.width(IntrinsicSize.Max)) {
            ShorterMonthFallback.values().forEach { fallback ->
                key(fallback) {
                    ShorterMonthFallbackPickerEntry(
                        text = { text(fallback) },
                        isSelected = { fallback == value.invoke() },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 8.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ), onClick = { onValueChange(fallback) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShorterMonthFallbackPickerEntry(
    text: () -> String,
    isSelected: () -> Boolean,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(enabled = Showcase.acceptsInputs, onClick = onClick)
            .padding(contentPadding)
    ) {
        Text(
            text = text.invoke(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ), modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(20.dp))
        RadioButton(
            selected = isSelected(),
            onClick = { onClick.invoke() },
            enabled = Showcase.acceptsInputs
        )
    }
}
