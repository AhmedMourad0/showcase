package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.compose.Showcase

@Composable
fun <T> InputList(
    name: String,
    addItemText: String,
    items: () -> List<T>,
    canDelete: () -> Boolean,
    onDelete: (index: Int, item: T) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
    itemKey: (index: Int, item: T) -> Any,
    itemContent: @Composable RowScope.(index: Int, item: T) -> Unit
) {
    Column(modifier) {
        Label(name)
        AddItemPlaceholder(
            text = addItemText,
            onClick = onAdd
        )
        items.invoke().forEachIndexed { index, item ->
            key(itemKey.invoke(index, item)) {
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
                    itemContent(index, item)
                    if (canDelete.invoke()) {
                        Spacer(Modifier.width(8.dp))
                        Image(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(enabled = Showcase.acceptsInputs) { onDelete(index, item) }
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
