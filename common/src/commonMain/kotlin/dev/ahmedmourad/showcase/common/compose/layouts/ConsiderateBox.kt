package dev.ahmedmourad.showcase.common.compose.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize

@Immutable
data class ConsiderateBoxData(
    val considerWidth: Boolean = false,
    val considerHeight: Boolean = false,
    val showIf: () -> Boolean = { true }
)

@Immutable
object ConsiderateBoxWidthModifier : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return (parentData as? ConsiderateBoxData)?.copy(considerWidth = true)
            ?: ConsiderateBoxData(considerWidth = true)
    }
}

@Immutable
object ConsiderateBoxHeightModifier : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return (parentData as? ConsiderateBoxData)?.copy(considerHeight = true)
            ?: ConsiderateBoxData(considerHeight = true)
    }
}

@Immutable
data class ConsiderateBoxShowIfModifier(val predicate: () -> Boolean) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return (parentData as? ConsiderateBoxData)?.copy(showIf = predicate)
            ?: ConsiderateBoxData(showIf = predicate)
    }
}

@Immutable
object ConsiderateBoxScope {
    fun Modifier.considerWidth() = this.then(ConsiderateBoxWidthModifier)
    fun Modifier.considerHeight() = this.then(ConsiderateBoxHeightModifier)
    fun Modifier.showIf(predicate: () -> Boolean) = this.then(ConsiderateBoxShowIfModifier(predicate))
}

@Immutable
enum class ConsiderateBoxSlotIds {
    Intermediate,
    Main
}

@Composable
fun ConsiderateBox(
    modifier: Modifier = Modifier,
    content: @Composable ConsiderateBoxScope.() -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val intermediatePlaceables = subcompose(ConsiderateBoxSlotIds.Intermediate) {
            content(ConsiderateBoxScope)
        }.map { it.measure(constraints) }
        val size = findMaxConsideredSize(intermediatePlaceables, constraints)
        val placeables = subcompose(ConsiderateBoxSlotIds.Main) {
            content(ConsiderateBoxScope)
        }.map {
            it.measure(Constraints(
                minWidth = constraints.minWidth,
                maxWidth = size.width,
                minHeight = constraints.minHeight,
                maxHeight = size.height
            ))
        }.filter {
            val parentData = it.parentData
            parentData as? ConsiderateBoxData == null || parentData.showIf.invoke()
        }
        layout(size.width, size.height) {
            placeables.forEach { it.placeRelative(0, 0) }
        }
    }
}

private fun findMaxConsideredSize(placeables: List<Placeable>, constraints: Constraints): IntSize {
    val maxConsideredWidth = placeables.filter {
        val parentData = it.parentData
        parentData as? ConsiderateBoxData != null && parentData.considerWidth
    }.maxOfOrNull {
        it.width
    }?.coerceIn(
        constraints.minWidth,
        constraints.maxWidth
    ) ?: placeables.maxOf { it.width }
    val maxConsideredHeight = placeables.filter {
        val parentData = it.parentData
        parentData as? ConsiderateBoxData != null && parentData.considerHeight
    }.maxOfOrNull {
        it.height
    }?.coerceIn(
        constraints.minHeight,
        constraints.maxHeight
    ) ?: placeables.maxOf { it.height }
    return IntSize(maxConsideredWidth, maxConsideredHeight)
}
