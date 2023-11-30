package dev.ahmedmourad.showcase.common.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.screens.canvas.CanvasViewModel
import dev.ahmedmourad.showcase.common.screens.datepickers.DatePickersViewModel
import dev.ahmedmourad.showcase.common.screens.home.CarouselState
import dev.ahmedmourad.showcase.common.screens.home.ScreensCarousel
import dev.ahmedmourad.showcase.common.screens.home.commonHomeScreens
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun HomeWindow(onCloseRequest: () -> Unit) {
    Window(
        state = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            width = 500.dp,
            height = 900.dp
        ), onCloseRequest = onCloseRequest,
        title = stringResource(RR.strings.app_name)
    ) {
        val canvasVM = remember { CanvasViewModel(Handle.Default) }
        val datePickersVM = remember { DatePickersViewModel(Handle.Default) }
        var carouselState by remember { mutableStateOf(CarouselState.Collapsed) }
        val screens = remember(canvasVM, datePickersVM) {
            commonHomeScreens(canvasVM, datePickersVM)
        }
        ScreensCarousel(
            state = { carouselState },
            onStateChange = { carouselState = it },
            screens = { screens },
            modifier = Modifier.fillMaxSize()
        )
    }
}
