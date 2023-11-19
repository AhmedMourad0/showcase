package dev.ahmedmourad.showcase.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.ahmedmourad.showcase.common.ViewModel
import dev.ahmedmourad.showcase.common.ios.UIControllerEvent
import dev.ahmedmourad.showcase.common.ios.UIControllerLifecycleEffect

@Composable
inline fun <T : ViewModel> viewModel(crossinline create: () -> T): T {
    val model = remember(create)
    UIControllerLifecycleEffect(UIControllerEvent.OnDestroy) {
        model.clear()
    }
    return model
}
