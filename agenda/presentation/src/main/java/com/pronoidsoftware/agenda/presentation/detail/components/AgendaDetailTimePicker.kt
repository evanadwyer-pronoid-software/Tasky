package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyTimePicker
import kotlinx.datetime.LocalTime

@Composable
fun AgendaDetailTimePicker(
    selectedTime: LocalTime,
    onSelectTime: (LocalTime) -> Unit,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    TaskyTimePicker(
        selectedTime = selectedTime,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        onSelectTime = onSelectTime,
        modifier = modifier,
    ) {
        IconButton(
            enabled = enabled,
            onClick = toggleExpanded,
            modifier = Modifier.alpha(if (enabled) 1f else 0f),
        ) {
            Icon(
                imageVector = ForwardChevronIcon,
                contentDescription = stringResource(id = R.string.edit_time),
                tint = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailTimePickerPreview() {
    TaskyTheme {
        var expanded by remember {
            mutableStateOf(false)
        }
        val toggleExpanded = {
            expanded = !expanded
        }
        AgendaDetailTimePicker(
            selectedTime = now().time,
            onSelectTime = { },
            expanded = expanded,
            toggleExpanded = toggleExpanded,
            enabled = true,
        )
    }
}
