package dev.ahmedmourad.showcase.common.screens.bungeegumbars

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.ViewModel

@Stable
open class BungeeGumBarsViewModel(handle: Handle) : ViewModel(handle) {
    val state = BungeeGumBarsState()
}

@Stable
class BungeeGumBarsState {
    val range1 = FloatRange(0f, 100f)
    var value1 by mutableStateOf(FloatRange(range1.end * 0.2f, range1.end * 0.8f))
    val range2 = FloatRange(0f, 100f)
    var value2 by mutableStateOf(FloatRange(range1.end * 0.2f, range1.end * 0.8f))
    val range3 = FloatRange(0f, 100f)
    var value3 by mutableStateOf(FloatRange(range1.end * 0.2f, range1.end * 0.8f))
    val range4 = FloatRange(0f, 100f)
    var value4 by mutableStateOf(FloatRange(range1.end * 0.2f, range1.end * 0.8f))
    val range5 = FloatRange(0f, 100f)
    var value5 by mutableStateOf(FloatRange(range1.end * 0.2f, range1.end * 0.8f))
}
