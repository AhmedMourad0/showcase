package dev.ahmedmourad.showcase.common.pickers.date

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.pickers.withYear
import dev.ahmedmourad.showcase.common.utils.format

@Composable
fun YearsView(
    state: DatePickerState,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp
) {
    LaunchedEffect(state.mode) {
        if (state.mode == DatePickerMode.Years) {
            gridState.animateScrollToItem(state.displayedYearIndex)
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        state = gridState,
        modifier = modifier.padding(horizontal = horizontalPadding)
    ) {
        items(items = state.yearRange.toList(), key = { it }) { year ->
            val selected = remember(state.displayed) {
                state.displayed.year == year
            }
            Box(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled = Showcase.acceptsInputs) {
                    state.displayed = state.displayed.withYear(year)
                    state.mode = DatePickerMode.Months
                }.padding(vertical = 16.dp)
            ) {
                Text(
                    text = remember { year.format() },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    ), modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
