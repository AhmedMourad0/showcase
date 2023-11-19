package dev.ahmedmourad.showcase.common.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import dev.ahmedmourad.showcase.common.home.ScreensCarousel
import dev.ahmedmourad.showcase.common.compose.debug.ThemeModeDebugger
import dev.ahmedmourad.showcase.common.home.CarouselScreen
import dev.ahmedmourad.showcase.common.home.CarouselState
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesUI
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesViewModel
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
        ThemeModeDebugger {
            val model = remember { MillionTimesViewModel(Handle.Default) }
            var carouselState by remember { mutableStateOf(CarouselState.Collapsed) }
            ScreensCarousel(
                state = carouselState,
                onStateChange = { carouselState = it },
                list = {
                    buildList {
                        add(CarouselScreen(title = "Million Times") {
                            val state by model.state.collectAsState()
                            MillionTimesUI(
                                state = state,
                                onStateChange = { model.state.value = it }
                            )
                        })
                    }
                }, modifier = Modifier.fillMaxSize()
            )
        }
    }
}
