package dev.ahmedmourad.showcase.common.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.canvas.CanvasUI
import dev.ahmedmourad.showcase.common.canvas.CanvasViewModel
import dev.ahmedmourad.showcase.common.home.CarouselScreen
import dev.ahmedmourad.showcase.common.home.CarouselState
import dev.ahmedmourad.showcase.common.home.ScreensCarousel
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesViewModel
import dev.ahmedmourad.showcase.common.pickers.date.DatePickersUI
import dev.ahmedmourad.showcase.common.pickers.date.DatePickersViewModel

@Stable
class ScreenMillionTimesViewModel(handle: SavedStateHandle) : MillionTimesViewModel(Handle(handle))

@Stable
class ScreenCanvasViewModel(handle: SavedStateHandle) : CanvasViewModel(Handle(handle))

@Stable
class ScreenDatePickersViewModel(handle: SavedStateHandle) : DatePickersViewModel(Handle(handle))

@Composable
fun HomeScreen() {
    val millionTimesVM: ScreenMillionTimesViewModel = viewModel()
    val canvasVM: ScreenCanvasViewModel = viewModel()
    val datePickersVM: ScreenDatePickersViewModel = viewModel()
    var carouselState by remember { mutableStateOf(CarouselState.Collapsed) }
    val screens = remember(millionTimesVM, canvasVM, datePickersVM) {
        buildList {
            add(CarouselScreen("Canvas") {
                CanvasUI(state = canvasVM.state)
            })
            add(CarouselScreen("Date Pickers") {
                val state by datePickersVM.state.collectAsState()
                DatePickersUI(
                    state = { state },
                    onStateChange = { datePickersVM.state.value = it }
                )
            })
//            add(CarouselScreen("Million Times") {
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
    BackHandler(enabled = { carouselState == CarouselState.Expanded }) {
        carouselState = CarouselState.Collapsed
    }
}

/**
 * enabled as a lambda to reduce recompositions to this function when the states
 * needed to calculate it change
 */
@Composable
fun BackHandler(enabled: () -> Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled(), onBack = onBack)
}
