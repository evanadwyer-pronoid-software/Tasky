package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
import com.pronoidsoftware.agenda.presentation.util.AgendaOverviewItemUiParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailActionText(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        TextButton(
            enabled = enabled,
            onClick = onClick,
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    color = TaskyGray,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailActionTextPreview(
    @PreviewParameter(AgendaOverviewItemUiParameterProvider::class) type: AgendaItemType,
) {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            when (type) {
                AgendaItemType.EVENT -> {
                    AgendaDetailActionText(text = "DELETE EVENT")
                    AgendaDetailActionText(text = "DELETE EVENT", enabled = true)
                    AgendaDetailActionText(text = "LEAVE EVENT")
                    AgendaDetailActionText(text = "LEAVE EVENT", enabled = true)
                    AgendaDetailActionText(text = "JOIN EVENT")
                    AgendaDetailActionText(text = "JOIN EVENT", enabled = true)
                }

                AgendaItemType.TASK -> {
                    AgendaDetailActionText(text = "DELETE TASK")
                    AgendaDetailActionText(text = "DELETE TASK", enabled = true)
                }

                AgendaItemType.REMINDER -> {
                    AgendaDetailActionText(text = "DELETE REMINDER")
                    AgendaDetailActionText(text = "DELETE REMINDER", enabled = true)
                }
            }
        }
    }
}
