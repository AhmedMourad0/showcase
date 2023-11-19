package dev.ahmedmourad.showcase.common.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesUI
import dev.ahmedmourad.showcase.common.milliontimes.MillionTimesViewModel
import dev.ahmedmourad.showcase.common.navigation.Destination
import dev.ahmedmourad.showcase.common.navigation.viewModel

fun HomeScreen() = Destination<Unit>(title = null) {
    val model = viewModel { MillionTimesViewModel(Handle.Default) }
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
