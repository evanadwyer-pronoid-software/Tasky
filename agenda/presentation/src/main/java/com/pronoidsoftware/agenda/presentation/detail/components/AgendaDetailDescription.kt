package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.agenda.presentation.util.AgendaOverviewItemUiParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailDescription(
    description: String?,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    editEnabled: Boolean = false,
) {
    val contentColor = MaterialTheme.colorScheme.onBackground
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = description ?: stringResource(id = R.string.enter_description),
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 15.sp,
            ),
            color = if (description != null) {
                contentColor
            } else {
                TaskyDarkGray
            },
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            enabled = editEnabled,
            onClick = onEdit,
            modifier = Modifier.alpha(if (editEnabled) 1f else 0f),
        ) {
            Icon(
                imageVector = ForwardChevronIcon,
                contentDescription = stringResource(id = R.string.edit),
                tint = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailDescriptionPreview(
    @PreviewParameter(AgendaOverviewItemUiParameterProvider::class) type: AgendaOverviewItemUi,
) {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            when (type) {
                is AgendaOverviewItemUi.EventOverviewUi -> {
                    AgendaDetailDescription(
                        description = "Amet minim mollit non deserunt ullamco " +
                            "est sit aliqua dolor do amet sint. ",
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = "Amet minim mollit non deserunt ullamco " +
                            "est sit aliqua dolor do amet sint. ",
                        editEnabled = true,
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = null,
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        editEnabled = true,
                        description = null,
                        onEdit = { },
                    )
                }

                is AgendaOverviewItemUi.TaskOverviewUi -> {
                    AgendaDetailDescription(
                        description = "Weekly plan\n" +
                            "Role distribution",
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = "Weekly plan\n" +
                            "Role distribution",
                        editEnabled = true,
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = null,
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = null,
                        editEnabled = true,
                        onEdit = { },
                    )
                }

                is AgendaOverviewItemUi.ReminderOverviewUi -> {
                    AgendaDetailDescription(
                        description = "Weekly plan\nRole distribution",
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = "Weekly plan\n" +
                            "Role distribution",
                        editEnabled = true,
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = null,
                        onEdit = { },
                    )
                    AgendaDetailDescription(
                        description = null,
                        editEnabled = true,
                        onEdit = { },
                    )
                }
            }
        }
    }
}
