package dev.ahmedmourad.showcase.common.screens.datepickers

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.LocalDateParceler
import dev.ahmedmourad.showcase.common.Parcelable
import dev.ahmedmourad.showcase.common.Parcelize
import dev.ahmedmourad.showcase.common.TypeParceler
import dev.ahmedmourad.showcase.common.ViewModel
import dev.ahmedmourad.showcase.common.screens.datepickers.models.DayOfYear
import dev.ahmedmourad.showcase.common.screens.datepickers.models.ShorterMonthFallback
import dev.ahmedmourad.showcase.common.utils.now
import dev.ahmedmourad.showcase.common.utils.SaveableMutableStateFlow
import kotlinx.datetime.LocalDate

@Stable
open class DatePickersViewModel(handle: Handle) : ViewModel(handle) {
    val state = SaveableMutableStateFlow(DatePickersState(), handle)
}

@Parcelize
@TypeParceler<LocalDate, LocalDateParceler>
@Immutable
data class DatePickersState(
    val date: LocalDate = LocalDate.now(),
    val daysOfMonth: Set<Int> = emptySet(),
    val daysOfMonthFallback: ShorterMonthFallback? = null,
    val daysOfYear: Set<DayOfYear> = emptySet(),
    val daysOfYearFallback: ShorterMonthFallback? = null
) : Parcelable
