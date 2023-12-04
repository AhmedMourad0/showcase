package dev.ahmedmourad.showcase.common.compose.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AndroidShowcaseTheme(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit
) {
    ShowcaseTheme {
        Box(modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.background, shape)
            .semantics { testTagsAsResourceId = true }
        ) { content() }
    }
}
