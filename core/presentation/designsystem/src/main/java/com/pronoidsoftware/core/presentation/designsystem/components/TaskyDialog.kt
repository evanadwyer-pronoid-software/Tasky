package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.R
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyDialog(
    title: String,
    description: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.W700,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                ),
            )
        },
        text = {
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.W300,
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
    )
}

@Preview
@Composable
private fun DeleteDialogPreview_Delete() {
    TaskyTheme {
        TaskyDialog(
            title = "Delete this reminder?",
            description = "This cannot be undone.",
            onCancel = { },
            onConfirm = { },
        )
    }
}

@Preview
@Composable
private fun DeleteDialogPreview_ExitEdit() {
    TaskyTheme {
        TaskyDialog(
            title = "Exit without saving?",
            description = "Changes will not be saved.",
            onCancel = { },
            onConfirm = { },
        )
    }
}
