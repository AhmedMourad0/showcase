package dev.ahmedmourad.showcase.common.screens.datepickers.pickers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.ahmedmourad.showcase.common.ModelType
import dev.ahmedmourad.showcase.common.OptionalDialog
import dev.ahmedmourad.showcase.common.compose.Showcase
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.LocalDate
import dev.ahmedmourad.showcase.common.*
import kotlin.jvm.JvmName

@JvmName("MultipleDatePickerDialog")
@Composable
fun DatePickerDialog(
    show: Boolean,
    selected: () -> List<LocalDate>,
    onSelectedChange: (List<LocalDate>) -> Unit,
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseDatePickerDialog(
        show = show,
        selected = selected,
        onSelectedChange = onSelectedChange,
        state = state,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

@Composable
fun DatePickerDialog(
    show: Boolean,
    selected: () -> LocalDate,
    onSelectedChange: (LocalDate) -> Unit,
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseDatePickerDialog(
        show = show,
        selected = { listOf(selected.invoke()) },
        onSelectedChange = { new ->
            onSelectedChange(new.last())
        }, state = state,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

@Composable
fun BaseDatePickerDialog(
    show: Boolean,
    selected: () -> List<LocalDate>,
    onSelectedChange: (List<LocalDate>) -> Unit,
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    OptionalDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        desktopType = ModelType.Popup(Modifier.width(328.dp)),
        mobileType = ModelType.BottomSheet()
    ) {
        DatePicker(
            selected = selected,
            onSelectedChange = onSelectedChange,
            state = state,
            modifier = modifier.padding(top = 12.dp, bottom = 20.dp)
        )
    }
}

@Composable
fun DialogActionBar(
    state: DatePickerState,
    onSelectedChange: (List<LocalDate>) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Spacer(Modifier.weight(1f))
        TextButton(
            shape = RoundedCornerShape(8.dp),
            onClick = onDismissRequest,
            enabled = Showcase.acceptsInputs,
            content = {
                Text(
                    text = stringResource(RR.strings.cancel),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        )
        TextButton(
            shape = RoundedCornerShape(8.dp),
            enabled = Showcase.acceptsInputs,
            onClick = {
                onDismissRequest.invoke()
            }, content = {
                Text(
                    text = stringResource(RR.strings.select),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        )
    }
}
