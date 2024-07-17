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
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDatePicker
import kotlinx.datetime.LocalDate

@Composable
fun AgendaDetailDatePicker(
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    enabled: Boolean = false,
) {
    TaskyDatePicker(
        selectedDate = selectedDate,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        onSelectDate = onSelectDate,
        modifier = modifier,
    ) {
        IconButton(
            enabled = enabled,
            onClick = toggleExpanded,
            modifier = Modifier.alpha(if (enabled) 100f else 0f),
        ) {
            Icon(
                imageVector = ForwardChevronIcon,
                contentDescription = stringResource(id = R.string.edit_date),
                tint = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailDatePickerPreview() {
    TaskyTheme {
        var expanded by remember {
            mutableStateOf(false)
        }
        val toggleExpanded = {
            expanded = !expanded
        }
        AgendaDetailDatePicker(
            selectedDate = today(),
            expanded = expanded,
            toggleExpanded = toggleExpanded,
            onSelectDate = { },
            enabled = true,
        )
    }
}
