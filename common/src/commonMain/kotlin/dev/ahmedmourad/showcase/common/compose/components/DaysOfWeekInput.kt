package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.*
import dev.ahmedmourad.showcase.common.pickers.desc
import dev.ahmedmourad.showcase.common.pickers.follows
import dev.ahmedmourad.showcase.common.pickers.getDisplayName
import dev.ahmedmourad.showcase.common.pickers.getShortDisplayName
import dev.ahmedmourad.showcase.common.pickers.joinToStringWithAnd
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

@Composable
fun TextFieldDaysOfWeekPicker(
    value: () -> Set<DayOfWeek>,
    onValueChange: (Set<DayOfWeek>) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        var showPicker by remember { mutableStateOf(false) }
        val text = remember(value.invoke()) {
            value.invoke().localized()
        }
        PickerHeader(
            text = text,
            maxLines = 1,
            onClick = { showPicker = true },
            leadingIcon = icon { Icons.Rounded.CalendarViewWeek },
            trailingIcon = icon { Icons.Rounded.KeyboardArrowDown },
            modifier = Modifier.fillMaxWidth()
        )
        DaysOfWeekPickerDialog(
            show = showPicker,
            value = value,
            onValueChange = onValueChange,
            onDismissRequest = { showPicker = false }
        )
    }
}

@Composable
fun DaysOfWeekPickerDialog(
    show: Boolean,
    value: () -> Set<DayOfWeek>,
    onValueChange: (Set<DayOfWeek>) -> Unit,
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
            DayOfWeek.values().forEach { day ->
                key(day) {
                    DaysOfWeekPickerEntry(
                        value = { day },
                        isSelected = { day in value() },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 8.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ), onClick = {
                            val selected = value.invoke()
                            onValueChange(if (day in selected) {
                                selected - day
                            } else {
                                selected + day
                            })
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DaysOfWeekPickerEntry(
    value: () -> DayOfWeek,
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
            text = value().getDisplayName(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ), modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(20.dp))
        Checkbox(
            checked = isSelected(),
            onCheckedChange = { onClick.invoke() },
            enabled = Showcase.acceptsInputs
        )
    }
}

private fun Set<DayOfWeek>.localized(): String {
    if (this.isEmpty()) return RR.strings.select_days_of_week.desc().get()
    if (this.size == 1) return this.first().getDisplayName()
    if (this.size == 2) return RR.strings.s_and_s.desc(this.first().getDisplayName(), this.last().getDisplayName()).get()
    if (this.size == DayOfWeek.values().size) return RR.strings.every_day.desc().get()
    val sorted = this.sortedBy { it.isoDayNumber }
    val breaks = sorted.fold(
        emptyList<Pair<DayOfWeek, DayOfWeek>>() to sorted.first()
    ) { (breaks, prevDay), day ->
        when {
            prevDay == day -> breaks
            day.follows(prevDay) -> breaks
            else -> breaks + (prevDay to day)
        } to day
    }.first
    return if (breaks.isEmpty()) {
        RR.strings.s_to_s.desc(sorted.first().getDisplayName(), sorted.last().getDisplayName()).get()
    } else if (breaks.size == 1 && this.containsAll(listOf(DayOfWeek(1), DayOfWeek(7)))) {
        RR.strings.s_to_s.desc(breaks.first().second.getDisplayName(), breaks.first().first.getDisplayName()).get()
    } else {
        sorted.joinToStringWithAnd(postfix = "") { it.getShortDisplayName() }
    }
}
