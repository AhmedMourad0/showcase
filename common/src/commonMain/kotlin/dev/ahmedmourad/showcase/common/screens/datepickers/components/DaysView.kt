package dev.ahmedmourad.showcase.common.screens.datepickers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.*
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.get
import dev.icerock.moko.resources.compose.stringResource
import dev.ahmedmourad.showcase.common.*
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.DisabledTextOpacity
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.DatePickerState
import dev.ahmedmourad.showcase.common.utils.desc
import dev.ahmedmourad.showcase.common.utils.getDaysOfWeek
import dev.ahmedmourad.showcase.common.utils.getShortDisplayName
import dev.ahmedmourad.showcase.common.utils.isLeapYear
import dev.ahmedmourad.showcase.common.utils.length
import dev.ahmedmourad.showcase.common.utils.minusDays
import dev.ahmedmourad.showcase.common.utils.minusMonths
import dev.ahmedmourad.showcase.common.utils.now
import dev.ahmedmourad.showcase.common.utils.plusMonths
import dev.ahmedmourad.showcase.common.utils.toDp
import dev.ahmedmourad.showcase.common.utils.withDayOfMonth
import dev.ahmedmourad.showcase.common.utils.format

@Immutable
sealed interface DayState : Parcelable {

    val date: LocalDate
    val isToday: Boolean

    @Parcelize
    @TypeParceler<LocalDate, LocalDateParceler>
    @Immutable
    data class OutOfScope(
        override val date: LocalDate,
        override val isToday: Boolean
    ) : DayState

    @Parcelize
    @TypeParceler<LocalDate, LocalDateParceler>
    @Immutable
    data class Disabled(
        override val date: LocalDate,
        override val isToday: Boolean
    ) : DayState

    @Parcelize
    @TypeParceler<LocalDate, LocalDateParceler>
    @Immutable
    data class Available(
        override val date: LocalDate,
        override val isToday: Boolean
    ) : DayState

    @Immutable
    @TypeParceler<LocalDate, LocalDateParceler>
    @Parcelize
    data class Selected(
        override val date: LocalDate,
        override val isToday: Boolean
    ) : DayState
}

@Immutable
data class MonthDays(
    val startPadding: IntRange,
    val days: IntRange,
    val endPadding: IntRange
)

@Composable
fun DaysView(
    selected: () -> List<LocalDate>,
    onSelectedChange: (List<LocalDate>) -> Unit,
    state: DatePickerState,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        WeekDays(
            selected = selected,
            state = state,
            weekStart = state.weekStart,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )
        Spacer(modifier = Modifier.height(8.dp))
        val days = remember(selected.invoke(), state.weekStart, state.displayed) {
            getDaysOfMonth(
                weekStart = state.weekStart,
                month = state.displayed.month,
                year = state.displayed.year
            ).withState(
                monthDate = state.displayed,
                selected = selected.invoke()
            )
        }
        SingleMonth(
            days = days,
            onDayClick = { onSelectedChange(selected.invoke() + it.date) },
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )
    }
}

@Composable
private fun WeekDays(
    selected: () -> List<LocalDate>,
    state: DatePickerState,
    weekStart: DayOfWeek,
    modifier: Modifier = Modifier
) {
    val weekDays = remember(weekStart) { getDaysOfWeek(weekStart) }
    Row(modifier) {
        val colors = MaterialTheme.colorScheme
        val selectedWeekdays = remember(selected.invoke(), state.displayed) {
            selected.invoke().filter {
                it.year == state.displayed.year && it.month == state.displayed.month
            }.map { it.dayOfWeek }.toSet()
        }
        weekDays.forEach { day ->
            key(day) {
                val name = remember(day) {
                    day.getShortDisplayName()
                }
                val color = remember(colors.primary, day, selectedWeekdays) {
                    if (day in selectedWeekdays) {
                        colors.primary
                    } else {
                        colors.onSurface.copy(alpha = DisabledTextOpacity)
                    }
                }
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = color
                    ), modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SingleMonth(
    days: List<DayState>,
    onDayClick: (DayState) -> Unit,
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing),
        modifier = modifier.fillMaxWidth()
    ) {
        val rowSize = 7
        days.chunked(rowSize).forEachIndexed { index, chunk ->
            key(index) {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    chunk.forEachIndexed { index, day ->
                        key(index) {
                            SingleDay(
                                name = day.date.dayOfMonth.format(),
                                state = day,
                                onClick = { onDayClick(day) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    repeat(rowSize - chunk.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleDay(
    name: String,
    state: DayState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    val dayModifier = modifier
        .aspectRatio(1f)
        .clip(RoundedCornerShape(8.dp))
        .clickable(enabled = Showcase.acceptsInputs, onClick = onClick)
    when (state) {
        is DayState.OutOfScope -> {
            DayText(
                name = name,
                isToday = state.isToday,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledTextOpacity),
                todayColor = MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledTextOpacity),
                modifier = dayModifier
            )
        }
        is DayState.Disabled -> {
            DayText(
                name = name,
                isToday = state.isToday,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledTextOpacity),
                todayColor = MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledTextOpacity),
                modifier = dayModifier
            )
        }
        is DayState.Available -> {
            DayText(
                name = name,
                isToday = state.isToday,
                color = MaterialTheme.colorScheme.onBackground,
                todayColor = MaterialTheme.colorScheme.primary,
                modifier = dayModifier
            )
        }
        is DayState.Selected -> {
            DayText(
                name = name,
                isToday = state.isToday,
                color = MaterialTheme.colorScheme.onPrimary,
                todayColor = MaterialTheme.colorScheme.onPrimary,
                modifier = dayModifier.background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun DayText(
    name: String,
    isToday: Boolean,
    color: Color,
    todayColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = color
            ), modifier = Modifier.align(Alignment.Center)
        )
        if (isToday) {
            val density = LocalDensity.current
            val typography = MaterialTheme.typography
            val measurer = rememberTextMeasurer()
            val nameHeight = remember(typography.bodySmall, name, density, measurer) {
                with(density) {
                    measurer.measure(
                        text = name,
                        style = typography.bodySmall
                    ).size.height.toDp()
                }
            }
            val todayStyle = remember(todayColor) {
                TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
                    color = todayColor
                )
            }
            val todayHeight = remember(name, density, measurer) {
                measurer.measure(
                    text = RR.strings.today.desc().get(),
                    style = todayStyle
                ).size.height.toDp(density)
            }
            Text(
                text = stringResource(RR.strings.today),
                style = todayStyle,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = nameHeight.div(2).plus(todayHeight).plus(6.dp))
            )
        }
    }
}

private fun getDaysOfMonth(
    weekStart: DayOfWeek,
    month: Month,
    year: Int
): MonthDays {
    val monthStart = LocalDate(year, month, 1)
    val firstDayOfMonth = monthStart.dayOfWeek
    val startPadding = if (firstDayOfMonth.isoDayNumber < weekStart.isoDayNumber) {
        7 + firstDayOfMonth.isoDayNumber - weekStart.isoDayNumber
    } else {
        firstDayOfMonth.isoDayNumber - weekStart.isoDayNumber
    }
    val previousMonthEnd = monthStart.minusDays(1).dayOfMonth
    val startPaddingDays = if (startPadding > 0) {
        (previousMonthEnd - startPadding + 1)..previousMonthEnd
    } else {
        IntRange.EMPTY
    }
    val monthLength = month.length(isLeapYear(year))
    val endPadding = (6 * 7) - (monthLength + startPadding)
    val endPaddingDays = if (endPadding > 0) {
        1..endPadding
    } else {
        IntRange.EMPTY
    }
    return MonthDays(startPaddingDays, 1..monthLength, endPaddingDays)
}

fun MonthDays.withState(
    monthDate: LocalDate,
    selected: List<LocalDate>
): List<DayState> {
    val today = LocalDate.now()
    return this.startPadding.map { day ->
        val date = monthDate.minusMonths(1).withDayOfMonth(day)
        DayState.OutOfScope(
            date = date,
            isToday = date == today
        )
    } + this.days.map { day ->
        val date = monthDate.withDayOfMonth(day)
        if (date in selected) {
            DayState.Selected(
                date = date,
                isToday = date == today
            )
        } else {
            DayState.Available(
                date = date,
                isToday = date == today
            )
        }
    } + this.endPadding.map { day ->
        val date = monthDate.plusMonths(1).withDayOfMonth(day)
        DayState.OutOfScope(
            date = date,
            isToday = date == today
        )
    }
}
