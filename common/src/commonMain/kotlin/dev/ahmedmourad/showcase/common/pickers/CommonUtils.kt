package dev.ahmedmourad.showcase.common.pickers

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.get
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.*
import kotlinx.datetime.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

fun getDaysOfWeek(weekStart: DayOfWeek): List<DayOfWeek> {
    return DayOfWeek.values().sortedBy {
        if (it.isoDayNumber < weekStart.isoDayNumber) {
            7 + it.isoDayNumber - weekStart.isoDayNumber
        } else {
            it.isoDayNumber - weekStart.isoDayNumber
        }
    }
}

fun isLeapYear(year: Int) = year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)

fun LocalDate.lengthOfMonth(): Int {
    return month.length(isLeapYear(this.year))
}

fun Month.length(isLeapYear: Boolean): Int {
   return when (number) {
       4, 6, 9, 11 -> 30
       2 -> if (isLeapYear) 29 else 28
       else -> 31
   }
}

fun LocalDate.copy(
    year: Int = this.year,
    monthNumber: Int = this.monthNumber,
    dayOfMonth: Int = this.dayOfMonth
) = LocalDate(
    year = year,
    monthNumber = monthNumber,
    dayOfMonth = dayOfMonth.coerceAtMost(Month(monthNumber).length(isLeapYear(year)))
)

fun LocalDate.withDayOfMonth(dayOfMonth: Int) = LocalDate(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = dayOfMonth.coerceAtMost(this.month.length(isLeapYear(this.year)))
)

fun LocalDate.withMonth(monthNumber: Int) = LocalDate(
    year = this.year,
    monthNumber = monthNumber,
    dayOfMonth = this.dayOfMonth.coerceAtMost(Month(monthNumber).length(isLeapYear(this.year)))
)

fun LocalDate.withYear(year: Int) = LocalDate(
    year = year,
    monthNumber = this.monthNumber,
    dayOfMonth = this.dayOfMonth
)

fun LocalDate.minusDays(days: Int) = this.minus(DatePeriod(days = days))

fun LocalDate.minusMonths(months: Int) = this.minus(DatePeriod(months = months))

fun LocalDate.minusYears(years: Int) = this.minus(DatePeriod(years = years))

fun LocalDate.plusDays(days: Int) = this.plus(DatePeriod(days = days))

fun LocalDate.plusMonths(months: Int) = this.plus(DatePeriod(months = months))

fun LocalDate.plusYears(years: Int) = this.plus(DatePeriod(years = years))

fun LocalDate.plusPeriod(period: DatePeriod) = this.plus(period)

fun LocalTime.plusMinutes(minutes: Int) = this.atDate(LocalDate.now()).plusMinutes(minutes).time

fun Instant.toLocalDateTime() = this.toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.clearNanoseconds() = LocalDateTime(
    year = year,
    monthNumber = monthNumber,
    dayOfMonth = dayOfMonth,
    hour = hour,
    minute = minute,
    second = second,
    nanosecond = 0
)

fun LocalDateTime.Companion.now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime()
}

fun LocalDate.Companion.now() = LocalDateTime.now().date

fun LocalTime.Companion.now() = LocalDateTime.now().time

fun LocalDate.toEpochMillis(): Long {
    return this.atTime(0, 0).toEpochMillis()
}

fun Month.getDisplayName(): String = when (this.number) {
    1 -> RR.strings.january.desc()
    2 -> RR.strings.february.desc()
    3 -> RR.strings.march.desc()
    4 -> RR.strings.april.desc()
    5 -> RR.strings.may.desc()
    6 -> RR.strings.june.desc()
    7 -> RR.strings.july.desc()
    8 -> RR.strings.august.desc()
    9 -> RR.strings.september.desc()
    10 -> RR.strings.october.desc()
    11 -> RR.strings.november.desc()
    12 -> RR.strings.december.desc()
    else -> throw IllegalArgumentException("${this.number} is not a valid month")
}.get()

fun Month.getShortDisplayName(): String = when (this.number) {
    1 -> RR.strings.january_short.desc()
    2 -> RR.strings.february_short.desc()
    3 -> RR.strings.march_short.desc()
    4 -> RR.strings.april_short.desc()
    5 -> RR.strings.may_short.desc()
    6 -> RR.strings.june_short.desc()
    7 -> RR.strings.july_short.desc()
    8 -> RR.strings.august_short.desc()
    9 -> RR.strings.september_short.desc()
    10 -> RR.strings.october_short.desc()
    11 -> RR.strings.november_short.desc()
    12 -> RR.strings.december_short.desc()
    else -> throw IllegalArgumentException("${this.number} is not a valid month")
}.get()

fun DayOfWeek.getDisplayName(): String = when (this) {
    DayOfWeek.SATURDAY -> RR.strings.saturday.desc()
    DayOfWeek.SUNDAY -> RR.strings.sunday.desc()
    DayOfWeek.MONDAY -> RR.strings.monday.desc()
    DayOfWeek.TUESDAY -> RR.strings.tuesday.desc()
    DayOfWeek.WEDNESDAY -> RR.strings.wednesday.desc()
    DayOfWeek.THURSDAY -> RR.strings.thursday.desc()
    DayOfWeek.FRIDAY -> RR.strings.friday.desc()
}.get()

fun DayOfWeek.getShortDisplayName(): String = when (this) {
    DayOfWeek.SATURDAY -> RR.strings.saturday_short.desc()
    DayOfWeek.SUNDAY -> RR.strings.sunday_short.desc()
    DayOfWeek.MONDAY -> RR.strings.monday_short.desc()
    DayOfWeek.TUESDAY -> RR.strings.tuesday_short.desc()
    DayOfWeek.WEDNESDAY -> RR.strings.wednesday_short.desc()
    DayOfWeek.THURSDAY -> RR.strings.thursday_short.desc()
    DayOfWeek.FRIDAY -> RR.strings.friday_short.desc()
}.get()

fun StringResource.desc() = StringDesc.Resource(this)

fun StringResource.desc(vararg args: Any) = StringDesc.ResourceFormatted(this, *args)

fun PluralsResource.desc(number: Int) = StringDesc.Plural(this, number)

fun PluralsResource.desc(number:Int, vararg args: Any) = StringDesc.PluralFormatted(this, number, *args)

fun String.desc() = StringDesc.Raw(this)

fun String.capitalized() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun String.decapitalized() = this.replaceFirstChar { it.lowercase() }

@Composable
fun toDp(px: Int): Dp {
    return with(LocalDensity.current) {
        px.toDp()
    }
}

fun <T> List<T>.padEnd(until: Int, with: T): List<T> {
    return if (this.size < until) {
        this + List(until - this.size) { with }
    } else {
        this
    }
}

fun String.replaceAt(index: Int, replacement: String): String {
    return this.toCharArray()
        .map { it.toString() }
        .toMutableList()
        .apply { set(index, replacement) }
        .joinToString(separator = "")
}

fun String.removeAt(index: Int): String {
    return this.toCharArray()
        .map { it.toString() }
        .toMutableList()
        .apply { removeAt(index) }
        .joinToString(separator = "")
}

fun String.padAt(targetLength: Int, index: Int, char: Char): String {
    if (this.length >= targetLength) return this
    val padding = List(targetLength - this.length) { char }
    return when {
        index < 0 -> padding.joinToString(separator = "") + this
        index > this.lastIndex -> this + padding.joinToString(separator = "")
        else -> this.toCharArray()
            .flatMapIndexed { i, c -> if (i == index) padding + c else listOf(c) }
            .joinToString(separator = "")
    }
}

fun LocalTime.withHour(hour: Int) = LocalTime(
    hour = hour.coerceIn(0, 23),
    minute = this.minute,
    second = this.second,
    nanosecond = this.nanosecond
)

fun LocalTime.withMinute(minute: Int) = LocalTime(
    hour = this.hour,
    minute = minute.coerceIn(0, 59),
    second = this.second,
    nanosecond = this.nanosecond
)

fun LocalTime.clearSecondsAndNanos() = LocalTime(
    hour = this.hour,
    minute = this.minute,
    second = 0,
    nanosecond = 0
)

fun LocalDateTime.withHour(hour: Int) = LocalDateTime(
    year = this.year,
    month = this.month,
    dayOfMonth = this.dayOfMonth,
    hour = hour.coerceIn(0, 23),
    minute = this.minute,
    second = this.second,
    nanosecond = this.nanosecond
)

fun LocalDateTime.withMinute(minute: Int) = LocalDateTime(
    year = this.year,
    month = this.month,
    dayOfMonth = this.dayOfMonth,
    hour = this.hour,
    minute = minute.coerceIn(0, 59),
    second = this.second,
    nanosecond = this.nanosecond
)

fun LocalDateTime.withDate(date: LocalDate) = LocalDateTime(
    year = date.year,
    month = date.month,
    dayOfMonth = date.dayOfMonth,
    hour = this.hour,
    minute = this.minute,
    second = this.second,
    nanosecond = this.nanosecond
)

fun LocalDateTime.withYear(year: Int) = LocalDateTime(
    year = year,
    month = this.month,
    dayOfMonth = this.dayOfMonth,
    hour = this.hour,
    minute = this.minute,
    second = this.second,
    nanosecond = this.nanosecond
)

fun LocalDateTime.withTime(time: LocalTime) = LocalDateTime(
    year = this.year,
    month = this.month,
    dayOfMonth = this.dayOfMonth,
    hour = time.hour,
    minute = time.minute,
    second = time.second,
    nanosecond = time.nanosecond
)

fun DayOfWeek.follows(other: DayOfWeek): Boolean {
    return if (this.isoDayNumber == 1) {
        other.isoDayNumber == 7
    } else {
        this.ordinal - other.ordinal == 1
    }
}

fun DayOfWeek.thePreviousDay(): DayOfWeek {
    return if (this.isoDayNumber == 1) DayOfWeek(7) else DayOfWeek(this.isoDayNumber - 1)
}

fun LocalDateTime.plusDays(days: Int): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault()).plus(days.days).toLocalDateTime()
}

fun LocalDateTime.plus(duration: Duration): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault()).plus(duration).toLocalDateTime()
}

fun LocalDateTime.plusMinutes(minutes: Int): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault()).plus(minutes.minutes).toLocalDateTime()
}

fun LocalDateTime.minusMinutes(minutes: Int): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault()).minus(minutes.minutes).toLocalDateTime()
}

fun LocalDateTime.minus(duration: Duration): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault()).minus(duration).toLocalDateTime()
}

fun LocalDateTime.toEpochMillis(): Long {
    return this.clearNanoseconds().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun LocalDate.isToday() = this == LocalDate.now()

fun Int.toDp(density: Density) = with(density) {
    this@toDp.toDp()
}

fun Float.toDp(density: Density) = with(density) {
    this@toDp.toDp()
}

fun Dp.toPx(density: Density) = with(density) {
    this@toPx.toPx()
}

fun TextUnit.unsalable(density: Density): TextUnit = this / density.fontScale

fun getDigitWithPostfix(digit: Int): String {
    return when {
        digit in listOf(11, 12, 13) -> RR.strings.d_th.desc(digit).get()
        digit.toString().last() == '1' -> RR.strings.d_st.desc(digit).get()
        digit.toString().last() == '2' -> RR.strings.d_nd.desc(digit).get()
        digit.toString().last() == '3' -> RR.strings.d_rd.desc(digit).get()
        else -> RR.strings.d_th.desc(digit).get()
    }
}

fun <T> Collection<T>.joinToStringWithAnd(
    postfix: String,
    transform: (T) -> String
): String {
    if (this.size == 1) return transform(this.first()) + postfix
    if (this.size == 2) return RR.strings.s_and_s.desc(transform(this.first()), transform(this.last())).get() + postfix
    return this.joinToString(
        separator = "",
        postfix = postfix
    ) { item ->
        when (item) {
            this.last() -> RR.strings.comma_and_s.desc(transform(item)).get()
            this.first() -> transform(item)
            else -> RR.strings.comma_s.desc(transform(item)).get()
        }
    }
}

fun <T> lambda(block: () -> T): () -> T = block

@OptIn(ExperimentalContracts::class)
fun <T> lambdaOrNull(condition: Boolean, block: () -> T): (() -> T)? {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        returnsNotNull() implies condition
    }
    return if (condition) block else null
}

@OptIn(ExperimentalContracts::class)
inline fun <T : Any> lambdaIfNotNull(subject: T?, crossinline block: (T) -> Unit): (() -> Unit)? {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        returnsNotNull() implies (subject != null)
    }
    return if (subject != null) lambda { block(subject) } else null
}

inline fun <T : Any> T?.otherwise(crossinline ifNull: () -> T): T = this ?: ifNull()

inline fun <reified To> Any.castOrNull(): To? = this as? To
