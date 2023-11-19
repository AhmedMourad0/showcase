package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp)
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onBackground
        ), modifier = modifier.padding(contentPadding)
    )
}
