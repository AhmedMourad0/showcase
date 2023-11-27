package dev.ahmedmourad.showcase.common.pickers.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.ahmedmourad.showcase.common.compose.components.Label
import dev.ahmedmourad.showcase.common.compose.components.LabelVerticalPadding
import dev.ahmedmourad.showcase.common.compose.components.TextFieldDatePicker
import dev.ahmedmourad.showcase.common.compose.components.TextFieldDaysOfMonthPicker
import dev.ahmedmourad.showcase.common.compose.components.TextFieldDaysOfYearPicker
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.theme.VerticalPadding
import dev.ahmedmourad.showcase.common.compose.theme.VerticalSpacing

@Composable
fun DatePickersUI(
    state: () -> DatePickersState,
    onStateChange: (DatePickersState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())
        .padding(bottom = VerticalPadding)
    ) {
        Label(
            text = "Date Picker",
            contentPadding = PaddingValues(bottom = LabelVerticalPadding),
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        TextFieldDatePicker(
            value = { state().date },
            onValueChange = { onStateChange(state().copy(date = it)) },
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        Spacer(Modifier.height(VerticalSpacing))
        Label(
            text = "Days of Year Picker",
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        TextFieldDaysOfYearPicker(
            value = { state().daysOfYear },
            onValueChange = { onStateChange(state().copy(daysOfYear = it)) },
            fallback = { state().daysOfYearFallback },
            onFallbackChange = { onStateChange(state().copy(daysOfYearFallback = it)) },
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        Spacer(Modifier.height(VerticalSpacing))
        Label(
            text = "Days of Month Picker",
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
        TextFieldDaysOfMonthPicker(
            value = { state().daysOfMonth },
            onValueChange = { onStateChange(state().copy(daysOfMonth = it)) },
            fallback = { state().daysOfMonthFallback },
            onFallbackChange = { onStateChange(state().copy(daysOfYearFallback = it)) },
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )
    }
}
