package dev.ahmedmourad.showcase.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed interface ModelType {
    data object Dialog : ModelType
    data object DropdownMenu : ModelType
    data class Popup(val modifier: Modifier = Modifier) : ModelType
    data class BottomSheet(val modifier: Modifier = Modifier) : ModelType
}

@Composable
expect fun OptionalDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    mobileType: ModelType,
    desktopType: ModelType,
    content: @Composable () -> Unit
)
