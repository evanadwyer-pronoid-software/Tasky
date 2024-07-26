package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.components.Tick
import com.pronoidsoftware.agenda.presentation.util.AgendaItemTypeParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailTitle(
    title: String,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    isCompleted: Boolean = false,
    editEnabled: Boolean = false,
) {
    val contentColor = MaterialTheme.colorScheme.onBackground
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
            color = contentColor,
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
            color = contentColor,
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
private fun AgendaDetailTitlePreview(
    @PreviewParameter(AgendaItemTypeParameterProvider::class) type: AgendaItemType,
) {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            when (type) {
                AgendaItemType.EVENT -> {
                    AgendaDetailTitle(title = "Meeting", onEdit = { })
                    AgendaDetailTitle(title = "Meeting", onEdit = { }, editEnabled = true)
                }

                AgendaItemType.TASK -> {
                    AgendaDetailTitle(title = "Project X", onEdit = { })
                    AgendaDetailTitle(title = "Project X", editEnabled = true, onEdit = { })
                    AgendaDetailTitle(title = "Project X", isCompleted = true, onEdit = { })
                    AgendaDetailTitle(
                        title = "Project X",
                        isCompleted = true,
                        editEnabled = true,
                        onEdit = { },
                    )
                }

                AgendaItemType.REMINDER -> {
                    AgendaDetailTitle(title = "Project X", onEdit = { })
                    AgendaDetailTitle(title = "Project X", editEnabled = true, onEdit = { })
                }
            }
        }
    }
}
