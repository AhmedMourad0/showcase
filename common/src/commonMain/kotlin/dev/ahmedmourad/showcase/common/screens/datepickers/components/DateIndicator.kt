package dev.ahmedmourad.showcase.common.screens.datepickers.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.modifiers.mirror
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.DatePickerMode
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.datetime.DatePeriod

@Composable
fun DateIndicator(
    text: () -> AnnotatedString,
    mode: () -> DatePickerMode,
    onModeChange: (DatePickerMode) -> Unit,
    onDateChange: (DatePeriod) -> Unit,
    modifier: Modifier = Modifier,
    hasPreviousMonth: () -> Boolean = { true },
    hasNextMonth: () -> Boolean = { true },
    hasYears: Boolean = true,
    horizontalPadding: Dp = 12.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = horizontalPadding).height(IntrinsicSize.Min)
    ) {
        val layoutDirection = LocalLayoutDirection.current
        if (hasYears && mode.invoke() != DatePickerMode.Years) {
            IndicatorNavigationButton(
                icon = { painterResource(RR.images.double_arrow_left) },
                contentDescription = "Previous Year",
                onClick = { onDateChange(DatePeriod(years = -1)) },
                modifier = Modifier.mirror(layoutDirection).fillMaxHeight().weight(1f)
            )
        }
        if (mode.invoke() == DatePickerMode.Days) {
            IndicatorNavigationButton(
                icon = { painterResource(RR.images.arrow_left) },
                contentDescription = "Previous Month",
                onClick = { onDateChange(DatePeriod(months = -1)) },
                enabled = hasPreviousMonth.invoke(),
                modifier = Modifier.mirror(layoutDirection).fillMaxHeight().weight(1f)
            )
        }
        Box(Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(Showcase.acceptsInputs) {
                onModeChange(when (mode.invoke()) {
                    DatePickerMode.Days -> DatePickerMode.Months
                    DatePickerMode.Months -> if (hasYears) DatePickerMode.Years else DatePickerMode.Days
                    DatePickerMode.Years -> DatePickerMode.Days
                })
            }.padding(vertical = 16.dp)
            .weight(if (mode.invoke() == DatePickerMode.Days) 3f else 5f)
        ) {
            Text(
                text = text.invoke(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ), modifier = Modifier.align(Alignment.Center)
            )
        }
        if (mode.invoke() == DatePickerMode.Days) {
            IndicatorNavigationButton(
                icon = { painterResource(RR.images.arrow_right) },
                contentDescription = "Next Month",
                onClick = { onDateChange(DatePeriod(months = 1)) },
                enabled = hasNextMonth.invoke(),
                modifier = Modifier.mirror(layoutDirection).fillMaxHeight().weight(1f)
            )
        }
        if (hasYears && mode.invoke() != DatePickerMode.Years) {
            IndicatorNavigationButton(
                icon = { painterResource(RR.images.double_arrow_right) },
                contentDescription = "Next Year",
                onClick = { onDateChange(DatePeriod(years = 1)) },
                modifier = Modifier.mirror(layoutDirection).fillMaxHeight().weight(1f)
            )
        }
    }
}

@Composable
private fun IndicatorNavigationButton(
    icon: @Composable () -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .clickable(enabled = Showcase.acceptsInputs && enabled, onClick = onClick)
        .alpha(if (enabled) 1f else 0.5f)
    ) {
        Image(
            painter = icon.invoke(),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(16.dp)
        )
    }
}
