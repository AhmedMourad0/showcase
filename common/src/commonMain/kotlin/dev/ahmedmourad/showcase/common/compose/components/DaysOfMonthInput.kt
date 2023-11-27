package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.pickers.date.component.models.ShorterMonthFallback
import kotlinx.datetime.LocalDate
import dev.ahmedmourad.showcase.common.*
import dev.ahmedmourad.showcase.common.pickers.date.component.MonthDays
import dev.ahmedmourad.showcase.common.pickers.date.component.SingleMonth
import dev.ahmedmourad.showcase.common.pickers.date.component.withState
import dev.ahmedmourad.showcase.common.pickers.desc
import dev.ahmedmourad.showcase.common.pickers.getDigitWithPostfix
import dev.ahmedmourad.showcase.common.pickers.joinToStringWithAnd
import dev.ahmedmourad.showcase.common.pickers.withDayOfMonth
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun TextFieldDaysOfMonthPicker(
    value: () -> Set<Int>,
    onValueChange: (Set<Int>) -> Unit,
    fallback: () -> ShorterMonthFallback?,
    onFallbackChange: (ShorterMonthFallback) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box {
            var showPicker by remember { mutableStateOf(false) }
            val text = remember(value.invoke()) {
                daysOfMonthLocalized(value.invoke())
            }
            PickerHeader(
                text = text,
                onClick = { showPicker = true },
                leadingIcon = icon { Icons.Rounded.CalendarViewMonth },
                trailingIcon = icon { Icons.Rounded.KeyboardArrowDown },
                modifier = Modifier.fillMaxWidth()
            )
            DaysOfMonthPickerDialog(
                show = showPicker,
                value = value,
                onValueChange = onValueChange,
                onDismissRequest = { showPicker = false }
            )
        }
        if (value.invoke().any { it >= 29 }) {
            Spacer(Modifier.height(12.dp))
            TextFieldShorterMonthFallbackPicker(
                value = fallback,
                onValueChange = onFallbackChange,
                text = { fallback ->
                    fallback.localizedForMonth(value.invoke().filter { it >= 29 }.min())
                }
            )
        }
    }
}

private fun ShorterMonthFallback?.localizedForMonth(earliestIllegalDay: Int): String = when (this) {
    ShorterMonthFallback.Bypass -> RR.strings.skip_for_the_month.desc().get()
    ShorterMonthFallback.FirstDayBefore -> RR.strings.last_day_of_month.desc().get()
    ShorterMonthFallback.FirstDayAfter -> RR.strings.first_day_of_next_month.desc().get()
    null -> RR.strings.for_months_shorter_than_d_days.desc(earliestIllegalDay).get()
}

@Composable
fun DaysOfMonthPickerDialog(
    show: Boolean,
    value: () -> Set<Int>,
    onValueChange: (Set<Int>) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    OptionalDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        desktopType = ModelType.Popup(Modifier.width(350.dp)),
        mobileType = ModelType.BottomSheet()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(12.dp)
        ) {
            Text(
                text = stringResource(RR.strings.select_days_of_month),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ), modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(Modifier.height(12.dp))
            val date = remember {
                LocalDate(
                    year = 2023,
                    monthNumber = 1,
                    dayOfMonth = 1
                )
            }
            val monthDays = remember {
                MonthDays(
                    startPadding = IntRange.EMPTY,
                    days = 1..31,
                    endPadding = IntRange.EMPTY
                )
            }
            val days = remember(value.invoke()) {
                monthDays.withState(
                    monthDate = date,
                    selected = value.invoke().map { date.withDayOfMonth(it) }
                )
            }
            SingleMonth(
                days = days,
                onDayClick = { day ->
                    onValueChange(
                        if (day.date.dayOfMonth in value.invoke()) {
                            value.invoke() - day.date.dayOfMonth
                        } else {
                            value.invoke() + day.date.dayOfMonth
                        }
                    )
                }, spacing = 4.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun daysOfMonthLocalized(value: Set<Int>): String {
    val postfix = " ${RR.strings.of_every_month.desc().get()}"
    val sorted = value.sorted()
    if (sorted.isEmpty()) return RR.strings.select_days_of_month.desc().get()
    return sorted.joinToStringWithAnd(postfix = postfix, transform = ::getDigitWithPostfix)
}
