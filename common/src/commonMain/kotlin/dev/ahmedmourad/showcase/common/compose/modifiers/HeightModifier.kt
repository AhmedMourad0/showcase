package dev.ahmedmourad.showcase.common.compose.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

fun Modifier.height(ratio: Float) = this.then(layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.width, (placeable.height * ratio).toInt()) {
        placeable.placeRelative(0, 0)
    }
})
