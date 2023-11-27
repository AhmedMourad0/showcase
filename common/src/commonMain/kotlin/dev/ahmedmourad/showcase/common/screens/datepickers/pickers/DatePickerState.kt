package dev.ahmedmourad.showcase.common.screens.datepickers.pickers

import androidx.compose.runtime.*
import dev.ahmedmourad.showcase.common.utils.now
import dev.ahmedmourad.showcase.common.utils.withDayOfMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Immutable
enum class DatePickerMode {
    Days, Months, Years
}

@Stable
class DatePickerState(
    initialDate: LocalDate,
    val yearRange: IntRange,
    val weekStart: DayOfWeek
) {
    var displayed by mutableStateOf(initialDate.withDayOfMonth(1))
    var mode by mutableStateOf(DatePickerMode.Days)
    val displayedYearIndex: Int get() = displayed.year - yearRange.first
}

@Composable
fun rememberDatePickerState(
    initialDate: LocalDate = LocalDate.now(),
    yearRange: IntRange = 1970..2100,
    weekStart: DayOfWeek = DayOfWeek.SATURDAY
): DatePickerState {
    return remember(initialDate, yearRange, weekStart) {
        DatePickerState(initialDate, yearRange, weekStart = weekStart)
    }
}
