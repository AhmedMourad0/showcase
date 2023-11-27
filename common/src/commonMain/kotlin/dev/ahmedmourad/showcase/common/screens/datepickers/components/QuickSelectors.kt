package dev.ahmedmourad.showcase.common.screens.datepickers.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.get
import dev.ahmedmourad.showcase.common.screens.datepickers.pickers.DatePickerMode
import dev.ahmedmourad.showcase.common.utils.desc
import dev.ahmedmourad.showcase.common.utils.now
import dev.ahmedmourad.showcase.common.utils.plusDays
import dev.ahmedmourad.showcase.common.utils.plusMonths
import dev.ahmedmourad.showcase.common.utils.plusYears
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.LocalDate

@Composable
fun QuickSelectorsBar(
    mode: () -> DatePickerMode,
    onModeChange: (DatePickerMode) -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onGotoSelected: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        QuickSelector(
            name = stringResource(RR.strings.today),
            modifier = Modifier.fillMaxHeight()
        ) {
            onSelectDate(LocalDate.now())
            onModeChange(DatePickerMode.Days)
        }
        Spacer(Modifier.width(4.dp))
        val tomorrowName = remember(mode.invoke()) {
            when (mode.invoke()) {
                DatePickerMode.Days -> RR.strings.tomorrow.desc().get()
                DatePickerMode.Months -> RR.plurals.in_d_months.desc(1).get()
                DatePickerMode.Years -> RR.plurals.in_d_years.desc(1).get()
            }
        }
        QuickSelector(
            name = tomorrowName,
            modifier = Modifier.fillMaxHeight()
        ) {
            val date = when (mode.invoke()) {
                DatePickerMode.Days -> LocalDate.now().plusDays(1)
                DatePickerMode.Months -> LocalDate.now().plusMonths(1)
                DatePickerMode.Years -> LocalDate.now().plusYears(1)
            }
            onSelectDate(date)
            onModeChange(DatePickerMode.Days)
        }
        if (onGotoSelected != null) {
            Spacer(Modifier.weight(1f))
            GotoSelectedSelector(
                onClick = onGotoSelected,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Composable
private fun QuickSelector(
    name: String,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = Showcase.acceptsInputs, onClick = onSelect)
            .padding(8.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun GotoSelectedSelector(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = Showcase.acceptsInputs, onClick = onClick)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(RR.images.my_location),
            contentDescription = "Go to selected",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.width(20.dp)
        )
    }
}
