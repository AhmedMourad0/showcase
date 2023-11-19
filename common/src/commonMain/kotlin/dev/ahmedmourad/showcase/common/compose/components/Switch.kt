package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.DisabledTextOpacity

@Composable
fun ShowcaseSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    @Suppress("NAME_SHADOWING")
    val checked by rememberUpdatedState(checked)
    val alignment by animateAlignmentAsState(if (checked) 1f else -1f)
    Box(contentAlignment = alignment,
        modifier = modifier.background(
            color = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledTextOpacity),
            shape = CircleShape
        ).clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = Showcase.acceptsInputs && enabled
        ) { onCheckedChange(!checked) }

    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = if (checked) Icons.Rounded.Done else Icons.Rounded.Close,
                contentDescription = if (checked) "Enabled" else "Disabled",
                tint = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledTextOpacity),
                modifier = Modifier.padding(3.dp).fillMaxSize()
            )
        }
    }
}

@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}
