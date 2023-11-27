package dev.ahmedmourad.showcase.common.screens.datepickers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.compose.components.Label
import dev.ahmedmourad.showcase.common.compose.components.LabelVerticalPadding
import dev.ahmedmourad.showcase.common.compose.components.PickerHeader
import dev.ahmedmourad.showcase.common.compose.components.icon
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.theme.VerticalPadding
import dev.ahmedmourad.showcase.common.compose.theme.VerticalSpacing
import dev.ahmedmourad.showcase.common.get
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.DatePickerDialog
import dev.ahmedmourad.showcase.common.screens.datepickers.models.DayOfMonth
import dev.ahmedmourad.showcase.common.screens.datepickers.models.DayOfYear
import dev.ahmedmourad.showcase.common.screens.datepickers.models.ShorterMonthFallback
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.rememberDatePickerState
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.DaysOfMonthPickerDialog
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.DaysOfYearPickerDialog
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.TextFieldShorterMonthFallbackPicker
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.daysOfMonthLocalized
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.localized
import dev.ahmedmourad.showcase.common.utils.desc
import dev.ahmedmourad.showcase.common.utils.format
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun DatePickersUI(
    state: () -> DatePickersState,
    onStateChange: (DatePickersState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())
        .padding(bottom = VerticalPadding)
    ) {
        Label(
            text = "Date Picker",
            contentPadding = PaddingValues(bottom = LabelVerticalPadding),
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        TextFieldDatePicker(
            value = { state().date },
            onValueChange = { onStateChange(state().copy(date = it)) },
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        Spacer(Modifier.height(VerticalSpacing))
        Label(
            text = "Days of Year Picker",
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        TextFieldDaysOfYearPicker(
            value = { state().daysOfYear },
            onValueChange = { onStateChange(state().copy(daysOfYear = it)) },
            fallback = { state().daysOfYearFallback },
            onFallbackChange = { onStateChange(state().copy(daysOfYearFallback = it)) },
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        Spacer(Modifier.height(VerticalSpacing))
        Label(
            text = "Days of Month Picker",
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        TextFieldDaysOfMonthPicker(
            value = { state().daysOfMonth },
            onValueChange = { onStateChange(state().copy(daysOfMonth = it)) },
            fallback = { state().daysOfMonthFallback },
            onFallbackChange = { onStateChange(state().copy(daysOfYearFallback = it)) },
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
    }
}

@Composable
private fun TextFieldDatePicker(
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
private fun TextFieldDaysOfYearPicker(
    value: () -> Set<DayOfYear>,
    onValueChange: (Set<DayOfYear>) -> Unit,
    fallback: () -> ShorterMonthFallback?,
    onFallbackChange: (ShorterMonthFallback) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box {
            var showPicker by remember { mutableStateOf(false) }
            val text = remember(value.invoke()) {
                value.invoke().localized()
            }
            PickerHeader(
                text = text,
                onClick = { showPicker = true },
                leadingIcon = icon { Icons.Rounded.CalendarMonth },
                trailingIcon = icon { Icons.Rounded.KeyboardArrowDown },
                modifier = Modifier.fillMaxWidth()
            )
            DaysOfYearPickerDialog(
                show = showPicker,
                value = value,
                onValueChange = onValueChange,
                onDismissRequest = { showPicker = false }
            )
        }
        val feb29 = remember {
            DayOfYear(
                dayOfMonth = DayOfMonth(29),
                month = Month.FEBRUARY
            )
        }
        if (value.invoke().any { it == feb29 }) {
            Spacer(Modifier.height(12.dp))
            TextFieldShorterMonthFallbackPicker(
                value = fallback,
                onValueChange = onFallbackChange,
                text = { it.localizedForYear() }
            )
        }
    }
}

private fun ShorterMonthFallback?.localizedForYear(): String = when (this) {
    ShorterMonthFallback.Bypass -> RR.strings.skip_for_the_year.desc().get()
    ShorterMonthFallback.FirstDayBefore -> RR.strings.last_day_of_february.desc().get()
    ShorterMonthFallback.FirstDayAfter -> RR.strings.first_day_of_march.desc().get()
    null -> RR.strings.for_non_leap_years.desc().get()
}

@Composable
private fun TextFieldDaysOfMonthPicker(
    value: () -> Set<Int>,
    onValueChange: (Set<Int>) -> Unit,
    fallback: () -> ShorterMonthFallback?,
    onFallbackChange: (ShorterMonthFallback) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box {
            var showPicker by remember { mutableStateOf(false) }
            val text = remember(value.invoke()) {
                daysOfMonthLocalized(value.invoke())
            }
            PickerHeader(
                text = text,
                onClick = { showPicker = true },
                leadingIcon = icon { Icons.Rounded.CalendarViewMonth },
                trailingIcon = icon { Icons.Rounded.KeyboardArrowDown },
                modifier = Modifier.fillMaxWidth()
            )
            DaysOfMonthPickerDialog(
                show = showPicker,
                value = value,
                onValueChange = onValueChange,
                onDismissRequest = { showPicker = false }
            )
        }
        if (value.invoke().any { it >= 29 }) {
            Spacer(Modifier.height(12.dp))
            TextFieldShorterMonthFallbackPicker(
                value = fallback,
                onValueChange = onFallbackChange,
                text = { fallback ->
                    fallback.localizedForMonth(value.invoke().filter { it >= 29 }.min())
                }
            )
        }
    }
}

private fun ShorterMonthFallback?.localizedForMonth(earliestIllegalDay: Int): String = when (this) {
    ShorterMonthFallback.Bypass -> RR.strings.skip_for_the_month.desc().get()
    ShorterMonthFallback.FirstDayBefore -> RR.strings.last_day_of_month.desc().get()
    ShorterMonthFallback.FirstDayAfter -> RR.strings.first_day_of_next_month.desc().get()
    null -> RR.strings.for_months_shorter_than_d_days.desc(earliestIllegalDay).get()
}
