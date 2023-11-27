package dev.ahmedmourad.showcase.common.pickers.date.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.layouts.ConsiderateBox
import dev.ahmedmourad.showcase.common.pickers.getDisplayName
import dev.ahmedmourad.showcase.common.pickers.lambdaOrNull
import dev.ahmedmourad.showcase.common.pickers.plusPeriod
import dev.ahmedmourad.showcase.common.pickers.withMonth
import dev.ahmedmourad.showcase.common.utils.format
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
fun DatePicker(
    selected: () -> List<LocalDate>,
    onSelectedChange: (List<LocalDate>) -> Unit,
    state: DatePickerState,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        val colors = MaterialTheme.colorScheme
        val indicatorText = remember(
            colors.primary,
            colors.onBackground,
            state.displayed,
            state.mode
        ) {
            buildCurrentPageText(
                mode = state.mode,
                date = state.displayed,
                selectedColor = colors.primary,
                regularColor = colors.onBackground
            )
        }
        DateIndicator(
            text = { indicatorText },
            mode = { state.mode },
            onModeChange = { state.mode = it },
            onDateChange = { state.displayed = state.displayed.plusPeriod(it) },
            hasYears = true
        )
        val yearsGridState = rememberLazyGridState(state.displayedYearIndex)
        CurrentPickerView(
            selected = selected,
            onSelectedChange = onSelectedChange,
            state = state,
            yearsGridState = yearsGridState,
            horizontalPadding = 16.dp
        )
        Spacer(modifier = Modifier.height(12.dp))
        val scope = rememberCoroutineScope()
        QuickSelectorsBar(
            mode = { state.mode },
            onModeChange = { state.mode = it },
            onSelectDate = {
                onSelectedChange(selected.invoke() + it)
                state.displayed = it
            }, onGotoSelected = lambdaOrNull(selected.invoke().size == 1) {
                state.displayed = selected.invoke().first()
                scope.launch {
                    yearsGridState.animateScrollToItem(state.displayedYearIndex)
                }
                Unit
            }, modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Start)
        )
    }
}

@Composable
fun CurrentPickerView(
    selected: () -> List<LocalDate>,
    onSelectedChange: (List<LocalDate>) -> Unit,
    state: DatePickerState,
    yearsGridState: LazyGridState,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp
) {
    ConsiderateBox(modifier) {
        DaysView(
            selected = selected,
            onSelectedChange = onSelectedChange,
            state = state,
            horizontalPadding = horizontalPadding,
            modifier = Modifier.considerHeight().showIf {
                state.mode == DatePickerMode.Days
            }
        )
        MonthsView(
            selected = { state.displayed.month },
            onSelectedChange = {
                state.displayed = state.displayed.withMonth(it.number)
                state.mode = DatePickerMode.Days
            }, horizontalPadding = horizontalPadding,
            modifier = Modifier.fillMaxHeight().showIf {
                state.mode == DatePickerMode.Months
            }
        )
        YearsView(
            state = state,
            gridState = yearsGridState,
            horizontalPadding = horizontalPadding,
            modifier = Modifier.fillMaxHeight().showIf {
                state.mode == DatePickerMode.Years
            }
        )
    }
}

private fun buildCurrentPageText(
    mode: DatePickerMode,
    date: LocalDate,
    selectedColor: Color,
    regularColor: Color
): AnnotatedString {
    return buildAnnotatedString {
        val selectedStyle = SpanStyle(color = selectedColor)
        val regularStyle = SpanStyle(color = regularColor)
        val monthsStyle = if (mode == DatePickerMode.Months) {
            selectedStyle
        } else {
            regularStyle
        }
        val yearsStyle = if (mode == DatePickerMode.Years) {
            selectedStyle
        } else {
            regularStyle
        }
        append("  ")
        withStyle(monthsStyle) {
            append(date.month.getDisplayName())
        }
        append("  ")
        withStyle(yearsStyle) {
            append(date.year.format())
        }
    }
}
