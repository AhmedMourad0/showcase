package dev.ahmedmourad.showcase.common.screens.datepickers.models

import androidx.compose.runtime.Immutable
import dev.ahmedmourad.showcase.common.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Parcelize
@TypeParceler<Month, MonthParceler>
@Immutable
data class DayOfYear(val dayOfMonth: DayOfMonth, val month: Month) : Parcelable

fun DayOfYear.withYear(year: Int): LocalDate = LocalDate(
    year = year,
    month = month,
    dayOfMonth = dayOfMonth.v
)

fun LocalDate.dayOfYear() = DayOfYear(
    dayOfMonth = DayOfMonth(this.dayOfMonth),
    month = this.month
)
