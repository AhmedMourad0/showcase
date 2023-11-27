package dev.ahmedmourad.showcase.common.screens.datepickers.pickers

import androidx.compose.foundation.layout.*
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
import kotlinx.datetime.LocalDate
import dev.ahmedmourad.showcase.common.*
import dev.ahmedmourad.showcase.common.screens.datepickers.components.DateIndicator
import dev.ahmedmourad.showcase.common.screens.datepickers.components.MonthDays
import dev.ahmedmourad.showcase.common.screens.datepickers.components.MonthsView
import dev.ahmedmourad.showcase.common.screens.datepickers.components.SingleMonth
import dev.ahmedmourad.showcase.common.screens.datepickers.models.DayOfYear
import dev.ahmedmourad.showcase.common.screens.datepickers.models.dayOfYear
import dev.ahmedmourad.showcase.common.screens.datepickers.models.withYear
import dev.ahmedmourad.showcase.common.screens.datepickers.components.withState
import dev.ahmedmourad.showcase.common.utils.desc
import dev.ahmedmourad.showcase.common.utils.getDigitWithPostfix
import dev.ahmedmourad.showcase.common.utils.getDisplayName
import dev.ahmedmourad.showcase.common.utils.getShortDisplayName
import dev.ahmedmourad.showcase.common.utils.joinToStringWithAnd
import dev.ahmedmourad.showcase.common.utils.lengthOfMonth
import dev.ahmedmourad.showcase.common.utils.now
import dev.ahmedmourad.showcase.common.utils.plusPeriod
import dev.ahmedmourad.showcase.common.utils.withMonth
import dev.ahmedmourad.showcase.common.utils.withYear
import kotlinx.datetime.number

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

fun Set<DayOfYear>.localized(): String {
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
