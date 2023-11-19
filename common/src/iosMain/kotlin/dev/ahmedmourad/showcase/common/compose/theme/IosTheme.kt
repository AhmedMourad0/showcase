package dev.ahmedmourad.showcase.common.compose.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IosShowcaseTheme(content: @Composable () -> Unit) {
    ShowcaseTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
//            .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            content()
        }
    }
}
