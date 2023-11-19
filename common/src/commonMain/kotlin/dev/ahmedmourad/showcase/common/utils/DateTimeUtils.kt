package dev.ahmedmourad.showcase.common.utils

import androidx.compose.runtime.Immutable
import dev.ahmedmourad.showcase.common.*
import kotlin.time.Duration

@Immutable
@Parcelize
data class SegregatedDuration(
    val years: Long,
    val months: Long,
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
) : Parcelable

fun Duration.segregated(): SegregatedDuration {
    val years = this.inWholeDays / 365
    val months = (this.inWholeDays - (years * 365)) / 30
    val days =  this.inWholeDays - ((years * 365) + (months * 30))
    return SegregatedDuration(
        years = years,
        months = months,
        days = days,
        hours = this.inWholeHours - (this.inWholeDays * 24),
        minutes = this.inWholeMinutes - (this.inWholeHours * 60),
        seconds = this.inWholeSeconds - (this.inWholeMinutes * 60)
    )
}
