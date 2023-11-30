package dev.ahmedmourad.showcase.common.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.navigation.Destination
import dev.ahmedmourad.showcase.common.navigation.viewModel
import dev.ahmedmourad.showcase.common.screens.canvas.CanvasViewModel
import dev.ahmedmourad.showcase.common.screens.datepickers.DatePickersViewModel

fun HomeScreen() = Destination<Unit>(title = null) {
    val canvasVM = viewModel { CanvasViewModel(Handle.Default) }
    val datePickersVM = viewModel { DatePickersViewModel(Handle.Default) }
    var carouselState by remember { mutableStateOf(CarouselState.Expanded) }
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
