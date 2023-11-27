package dev.ahmedmourad.showcase.common.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.RR
import dev.ahmedmourad.showcase.common.screens.canvas.CanvasUI
import dev.ahmedmourad.showcase.common.screens.canvas.CanvasViewModel
import dev.ahmedmourad.showcase.common.screens.home.ScreensCarousel
import dev.ahmedmourad.showcase.common.screens.home.CarouselScreen
import dev.ahmedmourad.showcase.common.screens.home.CarouselState
import dev.ahmedmourad.showcase.common.screens.milliontimes.MillionTimesUI
import dev.ahmedmourad.showcase.common.screens.milliontimes.MillionTimesViewModel
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
        val millionTimesVM = remember { MillionTimesViewModel(Handle.Default) }
        val canvasVM = remember { CanvasViewModel(Handle.Default) }
        var carouselState by remember { mutableStateOf(CarouselState.Collapsed) }
        val screens = remember(millionTimesVM, canvasVM) {
            buildList {
                add(CarouselScreen(title = "Canvas") {
                    CanvasUI(state = canvasVM.state)
                })
                add(CarouselScreen(title = "Million Times") {
                    val state by millionTimesVM.state.collectAsState()
                    MillionTimesUI(
                        state = { state },
                        onStateChange = { millionTimesVM.state.value = it }
                    )
                })
                add(CarouselScreen("screen #2") {
                    Box(Modifier.fillMaxSize().background(Color.Red))
                })
                add(CarouselScreen("screen #3") {
                    Box(Modifier.fillMaxSize().background(Color.Blue))
                })
            }
        }
        val screens1 = remember {
            buildList {
                add(CarouselScreen("screen #1") {
                    Box(Modifier.fillMaxSize().background(Color.Yellow))
                })
                add(CarouselScreen("screen #2") {
                    Box(Modifier.fillMaxSize().background(Color.Red))
                })
                add(CarouselScreen("screen #3") {
                    Box(Modifier.fillMaxSize().background(Color.Blue))
                })
            }
        }
        ScreensCarousel(
            state = { carouselState },
            onStateChange = { carouselState = it },
            screens = { screens },
            modifier = Modifier.fillMaxSize()
        )
    }
}
