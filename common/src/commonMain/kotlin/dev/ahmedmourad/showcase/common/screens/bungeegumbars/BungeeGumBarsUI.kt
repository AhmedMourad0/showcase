package dev.ahmedmourad.showcase.common.screens.bungeegumbars

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.theme.VerticalPadding

@Composable
fun BungeeGumBarsUI(
    state: BungeeGumBarsState,
    modifier: Modifier = Modifier
) {
    Column(modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(vertical = VerticalPadding, horizontal = HorizontalPadding)
    ) {
        BungeeGumRangeBar(
            value = { state.value1 },
            onValueChange = { state.value1 = it },
            range = { state.range1 },
            modifier = Modifier.fillMaxWidth().height(16.dp)
        )
        Spacer(Modifier.height(60.dp))
        BungeeGumRangeBar(
            value = { state.value2 },
            onValueChange = { state.value2 = it },
            range = { state.range2 },
            modifier = Modifier.fillMaxWidth().height(22.dp)
        )
        Spacer(Modifier.height(60.dp))
        BungeeGumRangeBar(
            value = { state.value3 },
            onValueChange = { state.value3 = it },
            range = { state.range3 },
            modifier = Modifier.fillMaxWidth().height(28.dp)
        )
        Spacer(Modifier.height(60.dp))
        BungeeGumRangeBar(
            value = { state.value4 },
            onValueChange = { state.value4 = it },
            range = { state.range4 },
            modifier = Modifier.fillMaxWidth().height(34.dp)
        )
        Spacer(Modifier.height(60.dp))
        BungeeGumRangeBar(
            value = { state.value5 },
            onValueChange = { state.value5 = it },
            range = { state.range5 },
            modifier = Modifier.fillMaxWidth().height(40.dp)
        )
    }
}
