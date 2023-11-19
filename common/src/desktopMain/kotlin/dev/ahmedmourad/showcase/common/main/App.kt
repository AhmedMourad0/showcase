package dev.ahmedmourad.showcase.common.main

import androidx.compose.ui.window.application
import dev.ahmedmourad.showcase.common.compose.theme.ShowcaseTheme

@Suppress("FunctionName")
fun App() = application {
    ShowcaseTheme {
        HomeWindow(onCloseRequest = ::exitApplication)
    }
}
