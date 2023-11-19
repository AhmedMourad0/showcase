package dev.ahmedmourad.showcase.common

import kotlinx.datetime.*
import kotlin.time.Duration

actual annotation class Parcelize
actual annotation class TypeParceler<T, P : Parceler<in T>>
actual annotation class IgnoredOnParcel
actual interface Parcelable
actual interface Parceler<T>

actual object InstantParceler : Parceler<Instant?>
actual object LocalDateTimeParceler : Parceler<LocalDateTime?>
actual object LocalDateParceler : Parceler<LocalDate?>
actual object LocalTimeParceler : Parceler<LocalTime?>
actual object DurationParceler : Parceler<Duration?>
actual object MonthParceler : Parceler<Month?>
