package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.domain.AgendaItem
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.util.AgendaOverviewItemUiParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailDescription(
    description: String,
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    onEdit: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = description,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 15.sp,
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isEditable) {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = ForwardChevronIcon,
                    contentDescription = stringResource(id = R.string.edit),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailDescriptionPreview(
    @PreviewParameter(AgendaOverviewItemUiParameterProvider::class) type: AgendaItem,
) {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            when (type) {
                AgendaItem.EVENT -> {
                    AgendaDetailDescription(
                        description = "Amet minim mollit non deserunt ullamco " +
                            "est sit aliqua dolor do amet sint. ",
                    )
                    AgendaDetailDescription(
                        description = "Amet minim mollit non deserunt ullamco " +
                            "est sit aliqua dolor do amet sint. ",
                        isEditable = true,
                    )
                }

                AgendaItem.TASK -> {
                    AgendaDetailDescription(
                        description = "Weekly plan\n" +
                            "Role distribution",
                    )
                    AgendaDetailDescription(
                        description = "Weekly plan\n" +
                            "Role distribution",
                        isEditable = true,
                    )
                }

                AgendaItem.REMINDER -> {
                    AgendaDetailDescription(description = "Weekly plan\nRole distribution")
                    AgendaDetailDescription(
                        description = "Weekly plan\n" +
                            "Role distribution",
                        isEditable = true,
                    )
                }
            }
        }
    }
}
