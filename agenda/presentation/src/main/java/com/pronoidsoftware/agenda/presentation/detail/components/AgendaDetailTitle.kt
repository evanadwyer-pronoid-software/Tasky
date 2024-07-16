package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.domain.AgendaItem
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.components.Tick
import com.pronoidsoftware.agenda.presentation.util.AgendaOverviewItemUiParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailTitle(
    title: String,
    modifier: Modifier = Modifier,
    isCompleted: Boolean = false,
    isEditable: Boolean = false,
    onEdit: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val textStyle = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.W700,
            fontSize = 26.sp,
            lineHeight = 25.sp,
        )
        Tick(
            color = TaskyBlack,
            ticked = isCompleted,
            radius = 10.dp,
            strokeWidth = 2.dp,
        )
        Spacer(modifier = Modifier.width(10.dp))
        val titleFormatted = buildAnnotatedString {
            withStyle(
                style = textStyle.toSpanStyle().copy(
                    textDecoration = if (isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                ),
            ) {
                append(title)
            }
        }
        Text(
            text = titleFormatted,
            style = textStyle,
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
private fun AgendaDetailTitlePreview(
    @PreviewParameter(AgendaOverviewItemUiParameterProvider::class) type: AgendaItem,
) {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            when (type) {
                AgendaItem.EVENT -> {
                    AgendaDetailTitle(title = "Meeting")
                    AgendaDetailTitle(title = "Meeting", isEditable = true)
                }

                AgendaItem.TASK -> {
                    AgendaDetailTitle(title = "Project X")
                    AgendaDetailTitle(title = "Project X", isEditable = true)
                    AgendaDetailTitle(title = "Project X", isCompleted = true)
                    AgendaDetailTitle(title = "Project X", isCompleted = true, isEditable = true)
                }

                AgendaItem.REMINDER -> {
                    AgendaDetailTitle(title = "Project X")
                    AgendaDetailTitle(title = "Project X", isEditable = true)
                }
            }
        }
    }
}
