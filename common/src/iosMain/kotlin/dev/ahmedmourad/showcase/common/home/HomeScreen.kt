package dev.ahmedmourad.showcase.common.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.canvas.CanvasUI
import dev.ahmedmourad.showcase.common.canvas.CanvasViewModel
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesViewModel
import dev.ahmedmourad.showcase.common.navigation.Destination
import dev.ahmedmourad.showcase.common.navigation.viewModel

fun HomeScreen() = Destination<Unit>(title = null) {
    val millionTimesVM = viewModel { MillionTimesViewModel(Handle.Default) }
    val canvasVM = viewModel { CanvasViewModel(Handle.Default) }
    var carouselState by remember { mutableStateOf(CarouselState.Expanded) }
    val screens = remember(millionTimesVM, canvasVM) {
        buildList {
            add(CarouselScreen(title = "Canvas") {
                CanvasUI(state = canvasVM.state)
            })
//            add(CarouselScreen(title = "Million Times") {
//                val state by millionTimesVM.state.collectAsState()
//                MillionTimesUI(
//                    state = { state },
//                    onStateChange = { millionTimesVM.state.value = it }
//                )
//            })
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
