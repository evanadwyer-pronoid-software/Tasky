@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.CloseIcon
import com.pronoidsoftware.core.presentation.designsystem.EditIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailToolbar(
    title: String,
    onXClick: () -> Unit,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val textStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 12.sp,
    )
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = textStyle,
                color = contentColor,
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        navigationIcon = {
            IconButton(onClick = onXClick) {
                Icon(
                    imageVector = CloseIcon,
                    tint = contentColor,
                    contentDescription = stringResource(
                        id = R.string.go_back_to_agenda_overview,
                    ),
                )
            }
        },
        actions = {
            if (isEditing) {
                TextButton(onClick = onSaveClick) {
                    Text(
                        text = stringResource(id = R.string.save),
                        style = textStyle,
                        color = contentColor,
                    )
                }
            } else {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = EditIcon,
                        contentDescription = stringResource(id = R.string.edit),
                        tint = contentColor,
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun AgendaDetailToolbarPreview_Read() {
    TaskyTheme {
        AgendaDetailToolbar(
            title = "01 MARCH 2022",
            onXClick = { },
            isEditing = false,
            onEditClick = { },
            onSaveClick = { },
        )
    }
}

@Preview
@Composable
private fun AgendaDetailToolbarPreview_Edit() {
    TaskyTheme {
        AgendaDetailToolbar(
            title = "EDIT REMINDER",
            onXClick = { },
            isEditing = true,
            onEditClick = { },
            onSaveClick = { },
        )
    }
}
