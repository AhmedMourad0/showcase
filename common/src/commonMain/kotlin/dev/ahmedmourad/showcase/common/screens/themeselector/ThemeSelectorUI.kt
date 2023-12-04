package dev.ahmedmourad.showcase.common.screens.themeselector

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.modifiers.boundedAspectRatio
import dev.ahmedmourad.showcase.common.compose.theme.HorizontalPadding
import dev.ahmedmourad.showcase.common.compose.theme.LocalThemeManager
import dev.ahmedmourad.showcase.common.compose.theme.VerticalPadding
import kotlinx.coroutines.launch

private const val RowSize = 2

@Composable
fun ThemeSelectorUI(
    state: ThemeSelectorState,
    modifier: Modifier = Modifier
) {
    val themeManager = LocalThemeManager.current
    val selectedTheme = themeManager.collectSelectedThemeState()
    val selectedIndex = remember(state.schemes, selectedTheme) {
        state.schemes
            .indexOfFirst { it.id == selectedTheme.id }
            .div(RowSize)
            .takeIf { it >= 0 }
    }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(selectedIndex ?: 0))
            .padding(vertical = VerticalPadding, horizontal = HorizontalPadding)
    ) {
        state.schemes.chunked(RowSize).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { schemes ->
                    key(schemes.id) {
                        ThemePreview(
                            schemes = schemes,
                            isSelected = { selectedTheme.id == schemes.id },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemePreview(
    schemes: ThemeSchemes,
    isSelected: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val themeManager = LocalThemeManager.current
    val scope = rememberCoroutineScope()
    Box(modifier
        .boundedAspectRatio(1f)
        .border(
            width = 2.dp,
            color = if (isSelected()) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            }, shape = RoundedCornerShape(8.dp)
        ).clip(RoundedCornerShape(8.dp))
        .clickable {
            scope.launch {
                themeManager.selectTheme(schemes)
            }
        }
    ) {
        ColorSchemePreview(
            scheme = schemes.light,
            modifier = Modifier.fillMaxSize().clip(CutCornerShape(bottomEndPercent = 100))
        )
        ColorSchemePreview(
            scheme = schemes.dark,
            modifier = Modifier.fillMaxSize().clip(CutCornerShape(topStartPercent = 100))
        )
    }
}

@Composable
private fun ColorSchemePreview(
    scheme: ColorScheme,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp)
) {
    Column(modifier
        .fillMaxSize()
        .background(scheme.background)
        .padding(contentPadding)
    ) {
        val cornerShape = RoundedCornerShape(4.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f)
        ) {
            Spacer(Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(scheme.primary, CircleShape)
            )
            Spacer(Modifier.fillMaxWidth(0.05f))
            Spacer(Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth()
                .background(scheme.secondary, cornerShape)
            )
        }
        Spacer(Modifier.fillMaxHeight(0.05f))
        Spacer(Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(scheme.tertiary, cornerShape)
        )
        Spacer(Modifier.fillMaxHeight(0.05f))
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f)
        ) {
            Spacer(Modifier
                .fillMaxHeight()
                .aspectRatio(3f)
                .background(scheme.primary, cornerShape)
            )
        }
    }
}
