package dev.ahmedmourad.showcase.common.screens.permissions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.ahmedmourad.showcase.common.R
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.ahmedmourad.showcase.common.compose.theme.VerticalSpacing

@Immutable
enum class RationaleReason {
    DeclinedOnce, DeclinedPermanently
}

@Immutable
data class RationaleContentScope(
    val permissions: List<Permission>,
    val reason: RationaleReason,
    val onDismiss: () -> Unit,
    val onAction: PermissionRationaleOnActionScope.() -> Unit
)

@Immutable
data class PermissionRationaleOnActionScope(
    val onDismiss: () -> Unit
)

@Composable
fun RationaleContentScope.RationaleDialog(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.permissions_required),
    rationale: (List<Permission>) -> String
) {
    Dialog(onDismissRequest = this@RationaleDialog.onDismiss) {
        Column(modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            RationaleText(
                title = title,
                permissions = { this@RationaleDialog.permissions },
                rationale = rationale
            )
            Spacer(Modifier.height(VerticalSpacing))
            Button(
                enabled = Showcase.acceptsInputs,
                onClick =  { this@RationaleDialog.onAction(PermissionRationaleOnActionScope(this@RationaleDialog.onDismiss)) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ){
                Text(text = stringResource(when (this@RationaleDialog.reason) {
                    RationaleReason.DeclinedOnce -> R.string.grant_permissions
                    RationaleReason.DeclinedPermanently -> R.string.open_app_settings
                }), style = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                ))
            }
        }
    }
}

@Composable
fun RationaleContentScope.RationaleContent(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.permissions_required),
    rationale: (List<Permission>) -> String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        RationaleText(
            title = title,
            permissions = { this@RationaleContent.permissions },
            rationale = rationale
        )
        Spacer(Modifier.height(VerticalSpacing))
        Button(
            enabled = Showcase.acceptsInputs,
            onClick =  { this@RationaleContent.onAction(PermissionRationaleOnActionScope(this@RationaleContent.onDismiss)) }
        ){
            Text(text = stringResource(when (this@RationaleContent.reason) {
                RationaleReason.DeclinedOnce -> R.string.grant_permissions
                RationaleReason.DeclinedPermanently -> R.string.open_app_settings
            }), style = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            ))
        }
    }
}

@Composable
private fun RationaleText(
    title: String,
    permissions: () -> List<Permission>,
    rationale: (List<Permission>) -> String,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        val text = remember(rationale, permissions()) {
            rationale(permissions())
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(VerticalSpacing))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier.fillMaxWidth()
        )
    }
}
