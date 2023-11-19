package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase

@Composable
fun IconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(6.dp),
    enabled: Boolean = true
) {
    IconButton(
        painter = { rememberVectorPainter(imageVector) },
        onClick = onClick,
        iconSize = iconSize,
        contentPadding = contentPadding,
        enabled = Showcase.acceptsInputs && enabled,
        modifier = modifier
    )
}

@Composable
fun IconButton(
    painter: @Composable () -> Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(6.dp),
    enabled: Boolean = true
) {
    Button(
        enabled = Showcase.acceptsInputs && enabled,
        onClick = onClick,
        contentPadding = contentPadding,
        shape = RoundedCornerShape(4.dp),
        content = {
            Image(
                painter = painter.invoke(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.size(iconSize)
            )
        }, modifier = modifier.defaultMinSize(1.dp, 1.dp)
    )
}
