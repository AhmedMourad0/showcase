package dev.ahmedmourad.showcase.common.screens.datepickers.pickers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.get
import dev.ahmedmourad.showcase.common.screens.datepickers.components.MonthDays
import dev.ahmedmourad.showcase.common.screens.datepickers.components.SingleMonth
import dev.ahmedmourad.showcase.common.screens.datepickers.components.withState
import dev.ahmedmourad.showcase.common.utils.desc
import dev.ahmedmourad.showcase.common.utils.getDigitWithPostfix
import dev.ahmedmourad.showcase.common.utils.joinToStringWithAnd
import dev.ahmedmourad.showcase.common.utils.withDayOfMonth
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.LocalDate

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

fun daysOfMonthLocalized(value: Set<Int>): String {
    val postfix = " ${RR.strings.of_every_month.desc().get()}"
    val sorted = value.sorted()
    if (sorted.isEmpty()) return RR.strings.select_days_of_month.desc().get()
    return sorted.joinToStringWithAnd(postfix = postfix, transform = ::getDigitWithPostfix)
}
