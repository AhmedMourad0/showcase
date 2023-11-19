package dev.ahmedmourad.showcase.common.compose.debug

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.theme.LocalThemeManager
import dev.ahmedmourad.showcase.common.compose.theme.VerticalPadding
import kotlinx.coroutines.launch

@Composable
fun ThemeModeDebugger(
    modifier: Modifier = Modifier,
    buttonAlignment: Alignment = Alignment.BottomEnd,
    content: @Composable () -> Unit
) {
    Box(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        content.invoke()
        val themeManager = LocalThemeManager.current
        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                scope.launch {
                    themeManager.toggleMode()
                }
            }, enabled = Showcase.acceptsInputs,
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(4.dp),
            modifier = Modifier
                .padding(horizontal = HorizontalPadding, vertical = VerticalPadding * 2)
                .size(46.dp)
                .align(buttonAlignment)
        ) {
            val isDark = themeManager.collectIsDarkAsState()
            Image(
                imageVector = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
