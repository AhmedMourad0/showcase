package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.*
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.modifiers.mirror
import dev.ahmedmourad.showcase.common.pickers.now
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.resources.desc.desc
import kotlinx.datetime.LocalDateTime

@Immutable
enum class UIDelayType {
    None, AtDate
}

@Immutable
@TypeParceler<LocalDateTime, LocalDateTimeParceler>
@Parcelize
data class UIStartDelayState(
    val type: UIDelayType = UIDelayType.None,
    val startsAt: LocalDateTime = LocalDateTime.now()
) : Parcelable

fun UIStartDelayState.isValid() = true

fun UIStartDelayState.createDelay() = when (type) {
    UIDelayType.None -> null
    UIDelayType.AtDate -> startsAt
}

@Composable
fun TextFieldStartDelayInput(
    value: () -> UIStartDelayState,
    onValueChange: (UIStartDelayState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box {
            var showPicker by remember { mutableStateOf(false) }
            PickerHeader(
                text = value.invoke().type.localized(),
                onClick = { showPicker = true },
                leadingIcon = icon { Icons.Rounded.Update },
                trailingIcon = icon { Icons.Rounded.KeyboardArrowDown },
                modifier = Modifier.fillMaxWidth()
            )
            StartDelayPickerDialog(
                show = showPicker,
                onSelect = {
                    onValueChange(value.invoke().copy(type = it))
                    showPicker = false
                }, onDismissRequest = { showPicker = false }
            )
        }
        when (value.invoke().type) {
            UIDelayType.None -> Unit
            UIDelayType.AtDate -> {
                Spacer(Modifier.height(12.dp))
                TextFieldDateTimePicker(
                    value = { value.invoke().startsAt },
                    onValueChange = { onValueChange(value.invoke().copy(startsAt = it)) }
                )
            }
        }
    }
}

@Composable
fun StartDelayPickerDialog(
    show: Boolean,
    onSelect: (UIDelayType) -> Unit,
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
            UIDelayType.values().forEach { entry ->
                key(entry) {
                    StartDelayPickerEntry(
                        text = { entry.localized() },
                        hasArrow = {
                            entry == UIDelayType.AtDate
                        }, onClick = { onSelect(entry) },
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun StartDelayPickerEntry(
    text: () -> String,
    hasArrow: () -> Boolean,
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
        if (hasArrow()) {
            Spacer(Modifier.width(20.dp))
            Image(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.mirror(LocalLayoutDirection.current)
            )
        }
    }
}

private fun UIDelayType.localized(): String {
    return when (this) {
        UIDelayType.None -> RR.strings.immediately.desc().get()
        UIDelayType.AtDate -> RR.strings.at_date.desc().get()
    }
}
