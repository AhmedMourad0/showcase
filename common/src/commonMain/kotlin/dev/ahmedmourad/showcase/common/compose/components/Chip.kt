package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.DisabledTextOpacity

@Composable
fun Chip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onBackground
            }.copy(alpha = if (Showcase.acceptsInputs) 1f else DisabledTextOpacity), textAlign = TextAlign.Center
        ), modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (isSelected) {
                    Modifier.background(MaterialTheme.colorScheme.primary)
                } else {
                    Modifier
                }
            ).clickable(enabled = Showcase.acceptsInputs, onClick = onClick)
            .border(
                width = 1.dp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }, shape = RoundedCornerShape(8.dp)
            ).padding(horizontal = 16.dp, vertical = 12.dp)
    )
}
