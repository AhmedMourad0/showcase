package dev.ahmedmourad.showcase.common.pickers.time

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.PreferenceManager
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.launchBlocker
import dev.ahmedmourad.showcase.common.defaultTimePickerType
import kotlinx.datetime.LocalTime

enum class TimePickerType {
    Wheel, Text, Keyboard
}

@Composable
fun TimePickerDialog(
    show: Boolean,
    value: () -> LocalTime,
    onValueChange: (LocalTime) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    OptionalDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        desktopType = ModelType.Popup(Modifier.wrapContentSize()),
        mobileType = ModelType.Dialog
    ) {
        Column(modifier.background(MaterialTheme.colorScheme.background).padding(horizontal = 24.dp)) {
            val prefManager = remember { PreferenceManager() }
            val preferredPicker by prefManager.preferredTimePicker(::defaultTimePickerType).collectAsState(defaultTimePickerType())
            when (preferredPicker) {
                TimePickerType.Wheel -> {
                    Spacer(Modifier.height(16.dp))
                    WheelTimePicker(
                        value = value,
                        onValueChange = onValueChange
                    )
                }
                TimePickerType.Text -> {
                    Spacer(Modifier.height(24.dp))
                    TextTimePicker(
                        value = value,
                        onValueChange = onValueChange
                    )
                    Spacer(Modifier.height(8.dp))
                }
                TimePickerType.Keyboard -> {
                    Spacer(Modifier.height(16.dp))
                    KeyboardTimePicker(
                        value = value,
                        onValueChange = onValueChange,
                        contentPadding = PaddingValues(0.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
            val scope = rememberCoroutineScope()
            Image(
                imageVector = when (preferredPicker) {
                    TimePickerType.Wheel -> Icons.Rounded.Keyboard
                    TimePickerType.Text -> Icons.Rounded.AccessTime //Icons.Rounded.Tag
                    TimePickerType.Keyboard -> Icons.Rounded.AccessTime
                }, contentDescription = "Switch Picker",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = Showcase.acceptsInputs) {
                        scope.launchBlocker {
                            prefManager.setPreferredTimePicker(
                                TimePickerType.values().getOrNull(
                                    preferredPicker.ordinal.plus(1).mod(TimePickerType.values().size - 1)
                                ) ?: defaultTimePickerType()
                            )
                        }
                    }.padding(8.dp)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}
