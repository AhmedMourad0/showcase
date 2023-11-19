package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.compose.layouts.ConsiderateBox
import dev.ahmedmourad.showcase.common.pickers.date.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import dev.ahmedmourad.showcase.common.*
import dev.ahmedmourad.showcase.common.pickers.date.model.DayOfMonth
import dev.ahmedmourad.showcase.common.pickers.date.model.DayOfYear
import dev.ahmedmourad.showcase.common.pickers.date.model.ShorterMonthFallback
import dev.ahmedmourad.showcase.common.pickers.date.model.dayOfYear
import dev.ahmedmourad.showcase.common.pickers.date.model.withYear
import dev.ahmedmourad.showcase.common.pickers.desc
import dev.ahmedmourad.showcase.common.pickers.getDigitWithPostfix
import dev.ahmedmourad.showcase.common.pickers.getDisplayName
import dev.ahmedmourad.showcase.common.pickers.getShortDisplayName
import dev.ahmedmourad.showcase.common.pickers.joinToStringWithAnd
import dev.ahmedmourad.showcase.common.pickers.lengthOfMonth
import dev.ahmedmourad.showcase.common.pickers.now
import dev.ahmedmourad.showcase.common.pickers.plusPeriod
import dev.ahmedmourad.showcase.common.pickers.withMonth
import dev.ahmedmourad.showcase.common.pickers.withYear
import kotlinx.datetime.number

@Composable
fun TextFieldDaysOfYearPicker(
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
fun DaysOfYearPickerDialog(
    show: Boolean,
    value: () -> Set<DayOfYear>,
    onValueChange: (Set<DayOfYear>) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    OptionalDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        desktopType = ModelType.Popup(Modifier.width(350.dp)),
        mobileType = ModelType.BottomSheet()
    ) {
        DaysOfYearPicker(
            selected = value,
            onSelectedChange = onValueChange,
            modifier = modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun DaysOfYearPicker(
    selected: () -> Set<DayOfYear>,
    onSelectedChange: (Set<DayOfYear>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        val colors = MaterialTheme.colorScheme
        var displayedDate by remember {
            mutableStateOf(LocalDate.now().withYear(2020))
        }
        var mode by remember { mutableStateOf(DatePickerMode.Days) }
        val indicatorText = remember(
            colors.primary,
            colors.onBackground,
            displayedDate,
            mode
        ) {
            buildAnnotatedString {
                val color = if (mode == DatePickerMode.Months) colors.primary else colors.onBackground
                withStyle(SpanStyle(color = color)) {
                    append(displayedDate.month.getDisplayName())
                }
            }
        }
        DateIndicator(
            text = { indicatorText },
            mode = { mode },
            onModeChange = { mode = it },
            onDateChange = { displayedDate = displayedDate.plusPeriod(it) },
            hasPreviousMonth = { displayedDate.monthNumber > 1 },
            hasNextMonth = { displayedDate.monthNumber < 12 },
            hasYears = false,
            horizontalPadding = 0.dp
        )
        CurrentPickerView(
            mode = mode,
            onModeChange = { mode = it },
            displayed = { displayedDate },
            onDisplayedChange = { displayedDate = it },
            selected = selected,
            onSelectedChange = onSelectedChange,
            horizontalPadding = 0.dp
        )
    }
}

@Composable
private fun CurrentPickerView(
    mode: DatePickerMode,
    onModeChange: (DatePickerMode) -> Unit,
    displayed: () -> LocalDate,
    onDisplayedChange: (LocalDate) -> Unit,
    selected: () -> Set<DayOfYear>,
    onSelectedChange: (Set<DayOfYear>) -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp
) {
    ConsiderateBox(modifier) {
        DaysView(
            displayed = displayed,
            selected = selected,
            onSelectedChange = onSelectedChange,
            horizontalPadding = horizontalPadding,
            modifier = Modifier.considerHeight().showIf {
                mode == DatePickerMode.Days
            }
        )
        MonthsView(
            selected = { displayed.invoke().month },
            onSelectedChange = {
                onDisplayedChange(displayed.invoke().withMonth(it.number))
                onModeChange(DatePickerMode.Days)
            }, horizontalPadding = horizontalPadding,
            modifier = Modifier.fillMaxHeight().showIf {
                mode == DatePickerMode.Months
            }
        )
    }
}

@Composable
private fun DaysView(
    displayed: () -> LocalDate,
    selected: () -> Set<DayOfYear>,
    onSelectedChange: (Set<DayOfYear>) -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp
) {
    val displayedDate = displayed.invoke()
    val monthDays = remember(displayedDate) {
        MonthDays(
            startPadding = IntRange.EMPTY,
            days = 1..displayedDate.lengthOfMonth(),
            endPadding = IntRange.EMPTY
        )
    }
    val days = remember(displayedDate, selected.invoke()) {
        monthDays.withState(
            monthDate = displayedDate,
            selected = selected.invoke().map { it.withYear(displayedDate.year) }
        )
    }
    SingleMonth(
        days = days,
        onDayClick = { day ->
            val dayOfYear = day.date.dayOfYear()
            val selectedDays = selected.invoke()
            onSelectedChange(if (dayOfYear in selectedDays) {
                selectedDays - dayOfYear
            } else {
                selectedDays + dayOfYear
            })
        }, spacing = 4.dp,
        modifier = modifier.padding(horizontal = horizontalPadding).fillMaxWidth()
    )
}

private fun Set<DayOfYear>.localized(): String {
    val postfix = " ${RR.strings.of_every_year.desc().get()}"
    val sorted = this.groupBy { it.month }
        .toList()
        .sortedBy { it.first }
        .map { (month, days) -> month to days.sortedBy { it.dayOfMonth.v } }
    if (sorted.isEmpty()) return RR.strings.select_days_of_year.desc().get()
    return sorted.toList().joinToStringWithAnd(
        postfix = postfix
    ) { (month, days) ->
        days.joinToStringWithAnd(
            postfix = " ${RR.strings.of_s.desc(month.getShortDisplayName()).get()}",
            transform = { getDigitWithPostfix(it.dayOfMonth.v) }
        )
    }
}
