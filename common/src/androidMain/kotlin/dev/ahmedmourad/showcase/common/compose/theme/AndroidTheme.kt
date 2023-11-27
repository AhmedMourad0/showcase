package dev.ahmedmourad.showcase.common.compose.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AndroidShowcaseTheme(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    ShowcaseTheme(provideColorScheme = { isDark ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isDark) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        } else {
            null
        }
    }) {
        Box(modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.background, shape)
            .semantics { testTagsAsResourceId = true }
        ) { content() }
    }
}
