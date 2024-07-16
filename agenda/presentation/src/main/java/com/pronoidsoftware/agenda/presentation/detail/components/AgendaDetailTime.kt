package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailTime(
    timeDescription: String,
    time: String,
    date: String,
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    onEditTime: () -> Unit,
    onEditDate: () -> Unit,
) {
    val textStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 15.sp,
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = timeDescription,
            style = textStyle,
        )
        Text(
            text = time,
            style = textStyle,
        )
        IconButton(
            enabled = isEditable,
            onClick = onEditTime,
            modifier = Modifier.alpha(if (isEditable) 1f else 0f),
        ) {
            Icon(
                imageVector = ForwardChevronIcon,
                contentDescription = stringResource(id = R.string.edit),
            )
        }
        Text(
            text = date,
            style = textStyle,
        )
        IconButton(
            enabled = isEditable,
            onClick = onEditDate,
            modifier = Modifier.alpha(if (isEditable) 100f else 0f),
        ) {
            Icon(
                imageVector = ForwardChevronIcon,
                contentDescription = stringResource(id = R.string.edit),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailTimePreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AgendaDetailTime(
                timeDescription = "At",
                time = "08:00",
                date = "July 21 2022",
                onEditTime = {},
                onEditDate = {},
            )
            AgendaDetailTime(
                timeDescription = "At",
                time = "08:00",
                date = "July 21 2022",
                onEditTime = {},
                onEditDate = {},
                isEditable = true,
            )
        }
    }
}
