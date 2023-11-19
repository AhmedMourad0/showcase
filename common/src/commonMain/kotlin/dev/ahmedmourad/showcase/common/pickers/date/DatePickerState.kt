package dev.ahmedmourad.showcase.common.pickers.date

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import dev.ahmedmourad.showcase.common.pickers.now
import dev.ahmedmourad.showcase.common.pickers.withDayOfMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

@Immutable
enum class DatePickerMode {
    Days, Months, Years
}

@Stable
class DatePickerState(
    val initialDate: LocalDate,
    val yearRange: IntRange,
    val weekStart: DayOfWeek
) {
    var displayed by mutableStateOf(initialDate.withDayOfMonth(1))
    var mode by mutableStateOf(DatePickerMode.Days)
    val displayedYearIndex: Int get() = displayed.year - yearRange.first
    companion object {
        val Saver: Saver<DatePickerState, *> = listSaver(
            save = {
                listOf<Any>(
                    it.initialDate.year,
                    it.initialDate.monthNumber,
                    it.initialDate.dayOfMonth,
                    it.yearRange.first,
                    it.yearRange.last,
                    it.weekStart.isoDayNumber,
                    it.displayed.year,
                    it.displayed.monthNumber,
                    it.displayed.dayOfMonth
                )
            }, restore = {
                DatePickerState(
                    initialDate = LocalDate(
                        it[0] as Int,
                        it[1] as Int,
                        it[2] as Int
                    ), yearRange = (it[3] as Int)..(it[4] as Int),
                    weekStart = DayOfWeek(it[5] as Int)
                ).apply {
                    displayed = LocalDate(
                        it[6] as Int,
                        it[7] as Int,
                        it[8] as Int
                    )
                }
            }
        )
    }
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
