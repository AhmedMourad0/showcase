package dev.ahmedmourad.showcase.common

import kotlinx.datetime.*
import kotlin.time.Duration

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize()

@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
expect annotation class TypeParceler<T, P : Parceler<in T>>()

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
expect annotation class IgnoredOnParcel()

expect interface Parcelable

expect interface Parceler<T>

expect object InstantParceler : Parceler<Instant?>
expect object LocalDateTimeParceler : Parceler<LocalDateTime?>
expect object LocalDateParceler : Parceler<LocalDate?>
expect object LocalTimeParceler : Parceler<LocalTime?>
expect object DurationParceler : Parceler<Duration?>
expect object MonthParceler : Parceler<Month?>
