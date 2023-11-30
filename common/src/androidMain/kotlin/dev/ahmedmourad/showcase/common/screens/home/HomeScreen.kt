package dev.ahmedmourad.showcase.common.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.screens.canvas.CanvasViewModel
import dev.ahmedmourad.showcase.common.screens.datepickers.DatePickersViewModel
import dev.ahmedmourad.showcase.common.screens.milliontimes.MillionTimesViewModel

@Stable
class ScreenMillionTimesViewModel(handle: SavedStateHandle) : MillionTimesViewModel(Handle(handle))

@Stable
class ScreenCanvasViewModel(handle: SavedStateHandle) : CanvasViewModel(Handle(handle))

@Stable
class ScreenDatePickersViewModel(handle: SavedStateHandle) : DatePickersViewModel(Handle(handle))

@Composable
fun HomeScreen() {
    val canvasVM: ScreenCanvasViewModel = viewModel()
    val datePickersVM: ScreenDatePickersViewModel = viewModel()
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
