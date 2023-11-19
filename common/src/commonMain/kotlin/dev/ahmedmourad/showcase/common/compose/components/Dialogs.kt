package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.*
import dev.icerock.moko.resources.desc.desc

//TODO: should use same actions mechanism at TextHeader
@Composable
fun ConfirmDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    description: String,
    confirmText: String,
    confirmColor: Color,
    modifier: Modifier = Modifier,
    confirmEnabled: Boolean = true,
    declineText: String = RR.strings.cancel.desc().get(),
    declineColor: Color = MaterialTheme.colorScheme.onBackground,
    onDecline: () -> Unit = onDismissRequest
) {
    OptionalDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        mobileType = ModelType.Dialog,
        desktopType = ModelType.Dialog
    ) {
        Column(modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp, start = 24.dp, end = 20.dp, bottom = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ), maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(Modifier.height(12.dp))
            Row {
                Spacer(Modifier.weight(1f))
                Text(
                    text = declineText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = declineColor
                    ), modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        .clickable(Showcase.acceptsInputs, onClick = onDecline)
                        .alpha(if (Showcase.acceptsInputs) 1f else 0.38f)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = confirmColor
                    ), modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        .clickable(confirmEnabled && Showcase.acceptsInputs, onClick = onConfirm)
                        .alpha(if (confirmEnabled && Showcase.acceptsInputs) 1f else 0.38f)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}
