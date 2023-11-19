package dev.ahmedmourad.showcase.common.pickers.date.model

import androidx.compose.runtime.Immutable

@Immutable
enum class ShorterMonthFallback {
    Bypass, FirstDayBefore, FirstDayAfter
}

@Immutable
data class DateInfo(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int
)
