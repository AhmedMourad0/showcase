package dev.ahmedmourad.showcase.common.pickers.date.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.pickers.getDisplayName
import kotlinx.datetime.Month
import kotlinx.datetime.number

@Composable
fun MonthsView(
    selected: () -> Month,
    onSelectedChange: (Month) -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp
) {
    Column(modifier.padding(horizontal = horizontalPadding)) {
        val monthChunks = remember {
            Month.values().asList().chunked(3)
        }
        monthChunks.forEach { chunk ->
            Row(Modifier.weight(1f)) {
                chunk.forEach { month ->
                    key(month.number) {
                        val text = remember(month) {
                            month.getDisplayName()
                        }
                        Box(Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(enabled = Showcase.acceptsInputs) {
                                onSelectedChange(month)
                            }
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected.invoke() == month) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground
                                    }, textAlign = TextAlign.Center
                                ), modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}
