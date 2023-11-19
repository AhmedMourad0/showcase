package dev.ahmedmourad.showcase.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun OptionalDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    mobileType: ModelType,
    desktopType: ModelType,
    content: @Composable () -> Unit
) {
    when (mobileType) {
        ModelType.DropdownMenu -> DropdownMenu(
            expanded = show,
            onDismissRequest = onDismissRequest,
            content = { content() }
        )
        ModelType.Dialog -> if (show) {
            Dialog(
                onDismissRequest = onDismissRequest,
                content = { Box(Modifier.clip(RoundedCornerShape(8.dp))) { content() } }
            )
        }
        is ModelType.Popup -> if (show) {
            Popup(
                alignment = Alignment.BottomStart,
                onDismissRequest = onDismissRequest,
                content = content
            )
        }
        is ModelType.BottomSheet -> if (show) {
            val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = onDismissRequest,
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                sheetState = state,
                dragHandle = null,
                content = {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
                    ) {
                        Spacer(Modifier
                            .size(width = 32.dp, height = 4.dp)
                            .clip(CircleShape)
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                        )
                    }
                    content()
                }, modifier = mobileType.modifier
            )
        }
    }
}
