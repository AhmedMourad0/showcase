package dev.ahmedmourad.showcase.common.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.canvas.CanvasUI
import dev.ahmedmourad.showcase.common.canvas.CanvasViewModel
import dev.ahmedmourad.showcase.common.home.CarouselScreen
import dev.ahmedmourad.showcase.common.home.CarouselState
import dev.ahmedmourad.showcase.common.home.ScreensCarousel
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesUI
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesViewModel
import dev.ahmedmourad.showcase.common.navigation.MainNavGraph

@Stable
class ScreenMillionTimesViewModel(handle: SavedStateHandle) : MillionTimesViewModel(Handle(handle))

@Stable
class ScreenCanvasViewModel(handle: SavedStateHandle) : CanvasViewModel(Handle(handle))

@MainNavGraph(start = true)
@Destination
@Composable
fun HomeScreen() {
    val millionTimesVM: ScreenMillionTimesViewModel = viewModel()
    val canvasVM: ScreenCanvasViewModel = viewModel()
    var carouselState by remember { mutableStateOf(CarouselState.Expanded) }
    ScreensCarousel(
        state = carouselState,
        onStateChange = { carouselState = it },
        list = {
            buildList {
                add(CarouselScreen(title = "Million Times") {
                    val state by millionTimesVM.state.collectAsState()
                    MillionTimesUI(
                        state = state,
                        onStateChange = { millionTimesVM.state.value = it }
                    )
                })
                add(0, CarouselScreen(title = "Canvas") {
                    CanvasUI(
                        items = { canvasVM.items },
                        onItemsChange = { canvasVM.items = it },
                        undoManager = canvasVM.undoManager,
                        focusManager = canvasVM.focusManager
                    )
                })
            }
        }, modifier = Modifier.fillMaxSize()
    )
    BackHandler(enabled = carouselState == CarouselState.Expanded) {
        carouselState = CarouselState.Collapsed
    }
}
