package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.LocalThemeManager
import kotlinx.coroutines.launch

@Composable
fun ActionButton(
    painter: Painter,
    onClick: () -> Unit,
    enabled: Boolean,
    ignoreInputBlocker: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    colorFilter: ColorFilter? = null
) {
    val isEnabled = enabled && (ignoreInputBlocker || Showcase.acceptsInputs)
    Box(modifier.clip(RoundedCornerShape(8.dp)).clickable(
        enabled = isEnabled,
        onClick = onClick
    ).alpha(if (isEnabled) 1f else 0.5f).padding(contentPadding)) {
        Image(
            painter = painter,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            colorFilter = colorFilter
        )
    }
}

@Composable
fun ActionButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    enabled: Boolean = true,
    ignoreInputBlocker: Boolean = false,
    colorFilter: ColorFilter? = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
) {
    ActionButton(
        painter = rememberVectorPainter(imageVector),
        onClick = onClick,
        enabled = enabled,
        ignoreInputBlocker = ignoreInputBlocker,
        colorFilter = colorFilter,
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
fun ThemeModeActionButton(
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null,
    enabled: Boolean = true
) {
    val themeManager = LocalThemeManager.current
    val scope = rememberCoroutineScope()
    val isDark = themeManager.collectIsDarkAsState()
    ActionButton(
        enabled = enabled,
        imageVector = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
        colorFilter = colorFilter,
        onClick = {
            scope.launch {
                themeManager.toggleMode()
            }
        }, ignoreInputBlocker = true, modifier = modifier
    )
}
