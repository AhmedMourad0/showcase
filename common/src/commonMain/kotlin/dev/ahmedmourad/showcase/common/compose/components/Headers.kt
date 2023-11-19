package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.modifiers.mirror
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.theme.LocalThemeManager
import dev.ahmedmourad.showcase.common.compose.theme.schemes.TestDarkScheme
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.launch

@Composable
fun NavigateUpButton(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Image(
        imageVector = Icons.Rounded.ArrowBack,
        contentDescription = null,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
            .mirror(LocalLayoutDirection.current)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                enabled = Showcase.acceptsInputs,
                onClick = { onNavigateUp.invoke() }
            ).padding(8.dp)
    )
}

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
    enabled: Boolean = true
) {
    val themeManager = LocalThemeManager.current
    val scope = rememberCoroutineScope()
    val isDark = themeManager.collectIsDarkAsState()
    ActionButton(
        enabled = enabled,
        imageVector = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
        onClick = {
            scope.launch {
                themeManager.toggleMode()
            }
        }, ignoreInputBlocker = true, modifier = modifier
    )
}

@Composable
fun NoTitleHeader(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = { },
    color: Color = MaterialTheme.colorScheme.onBackground,
    padding: PaddingValues = PaddingValues(top = 24.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(padding)
    ) {
        NavigateUpButton(onNavigateUp = onNavigateUp, color = color)
        Spacer(Modifier.weight(1f))
        actions.invoke(this)
    }
}

@Composable
fun TextHeader(
    title: String,
    onNavigateUp: (() -> Unit)?,
    hasLogo: Boolean = false,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    leadingActions: @Composable (RowScope.() -> Unit)? = null,
    trailingActions: @Composable (RowScope.() -> Unit)? = null
) {
    Surface(shadowElevation = elevation, modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                start = HorizontalPadding,
                end = HorizontalPadding,
                top = 16.dp,
                bottom = 12.dp
            ).height(40.dp)
        ) {
            if (onNavigateUp != null) {
                NavigateUpButton(
                    onNavigateUp = onNavigateUp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.size(16.dp))
            }
            if (leadingActions != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), content = leadingActions)
                Spacer(Modifier.width(8.dp))
            }
            if (hasLogo) {
                Box(Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(TestDarkScheme.background, CircleShape)
                    .padding(6.dp)
                ) {
                    Image(
                        painter = painterResource(RR.images.logo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(Modifier.size(6.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                ), modifier = Modifier.weight(1f)
            )
            if (trailingActions != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), content = trailingActions)
            }
        }
    }
}

@Composable
fun TwoLineTextHeader(
    title: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 26.sp,
    color: Color = MaterialTheme.colorScheme.onBackground,
    padding: PaddingValues = PaddingValues(top = 24.dp)
) {
    Column(modifier.fillMaxWidth().padding(padding)) {
        NavigateUpButton(
            onNavigateUp = onNavigateUp,
            color = color,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = title,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            color = color,
            modifier = Modifier
        )
    }
}
