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
enum class UIRepeatUntilType {
    Indefinitely, UntilDate
}

@Immutable
@TypeParceler<LocalDateTime, LocalDateTimeParceler>
@Parcelize
data class UIRepeatUntilState(
    val type: UIRepeatUntilType = UIRepeatUntilType.Indefinitely,
    val endsAt: LocalDateTime = LocalDateTime.now()
) : Parcelable

fun UIRepeatUntilState.isValid() = true

fun UIRepeatUntilState.createRepeatUntil() = when (type) {
    UIRepeatUntilType.Indefinitely -> null
    UIRepeatUntilType.UntilDate -> endsAt
}

@Composable
fun TextFieldRepeatUntilInput(
    value: () -> UIRepeatUntilState,
    onValueChange: (UIRepeatUntilState) -> Unit,
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
            RepeatUntilPickerDialog(
                show = showPicker,
                onSelect = {
                    onValueChange(value.invoke().copy(type = it))
                    showPicker = false
                }, onDismissRequest = { showPicker = false }
            )
        }
        when (value.invoke().type) {
            UIRepeatUntilType.Indefinitely -> Unit
            UIRepeatUntilType.UntilDate -> {
                Spacer(Modifier.height(12.dp))
                TextFieldDateTimePicker(
                    value = { value.invoke().endsAt },
                    onValueChange = { onValueChange(value.invoke().copy(endsAt = it)) }
                )
            }
        }
    }
}

@Composable
fun RepeatUntilPickerDialog(
    show: Boolean,
    onSelect: (UIRepeatUntilType) -> Unit,
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
            UIRepeatUntilType.values().forEach { entry ->
                key(entry) {
                    RepeatUntilPickerEntry(
                        text = { entry.localized() },
                        hasArrow = {
                            entry == UIRepeatUntilType.UntilDate
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
private fun RepeatUntilPickerEntry(
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

private fun UIRepeatUntilType.localized(): String {
    return when (this) {
        UIRepeatUntilType.Indefinitely -> RR.strings.indefinitely.desc().get()
        UIRepeatUntilType.UntilDate -> RR.strings.until_date.desc().get()
    }
}
