package com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.CheckIcon
import com.pronoidsoftware.core.presentation.designsystem.CloseIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyError
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyActionButton
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyTextField

@Composable
fun AddVisitorDialog(
    title: String,
    buttonText: String,
    onAddClick: (String) -> Unit,
    onCancel: () -> Unit,
    isAddingAttendee: Boolean,
    emailTextFieldState: TextFieldState,
    isEmailValid: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        containerColor = TaskyWhite,
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.W700,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                ),
                color = TaskyBlack,
            )
        },
        icon = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = stringResource(id = R.string.go_back),
                        tint = TaskyBlack,
                    )
                }
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TaskyTextField(
                    state = emailTextFieldState,
                    hint = stringResource(id = R.string.email_address),
                    endIcon = if (isEmailValid) {
                        CheckIcon
                    } else {
                        null
                    },
                    endIconContentDescription = if (isEmailValid) {
                        stringResource(id = R.string.email_is_valid)
                    } else {
                        null
                    },
                    error = emailTextFieldState.text.isNotBlank() && !isEmailValid,
                    keyboardType = KeyboardType.Email,
                )
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.W300,
                        fontSize = 14.sp,
                    ),
                    color = TaskyError,
                )
            }
        },
        onDismissRequest = onCancel,
        confirmButton = {
            TaskyActionButton(
                text = buttonText,
                isLoading = isAddingAttendee,
                isLoadingContentDescription = stringResource(id = R.string.adding_visitor),
                onClick = {
                    onAddClick(emailTextFieldState.text.toString())
                },
                enabled = !isAddingAttendee && isEmailValid,
            )
        },
    )
}

@Preview
@Composable
private fun AddVisitorDialogPreview_Empty() {
    TaskyTheme {
        AddVisitorDialog(
            title = "Add visitor",
            buttonText = "ADD",
            onAddClick = { },
            onCancel = { },
            isAddingAttendee = false,
            emailTextFieldState = TextFieldState(),
            isEmailValid = false,
            errorMessage = "",
        )
    }
}

@Preview
@Composable
private fun AddVisitorDialogPreview_Filled_Valid() {
    TaskyTheme {
        AddVisitorDialog(
            title = "Add visitor",
            buttonText = "ADD",
            onAddClick = { },
            onCancel = { },
            isAddingAttendee = false,
            emailTextFieldState = TextFieldState("tester@test.com"),
            isEmailValid = true,
            errorMessage = "",
        )
    }
}

@Preview
@Composable
private fun AddVisitorDialogPreview_Filled_Invalid() {
    TaskyTheme {
        AddVisitorDialog(
            title = "Add visitor",
            buttonText = "ADD",
            onAddClick = { },
            onCancel = { },
            isAddingAttendee = false,
            emailTextFieldState = TextFieldState("tester@test"),
            isEmailValid = false,
            errorMessage = "",
        )
    }
}

@Preview
@Composable
private fun AddVisitorDialogPreview_Loading() {
    TaskyTheme {
        AddVisitorDialog(
            title = "Add visitor",
            buttonText = "ADD",
            onAddClick = { },
            onCancel = { },
            isAddingAttendee = true,
            emailTextFieldState = TextFieldState("tester@test.com"),
            isEmailValid = true,
            errorMessage = "",
        )
    }
}

@Preview
@Composable
private fun AddVisitorDialogPreview_Error_UserNotFound() {
    TaskyTheme {
        AddVisitorDialog(
            title = "Add visitor",
            buttonText = "ADD",
            onAddClick = { },
            onCancel = { },
            isAddingAttendee = false,
            emailTextFieldState = TextFieldState("tester@test.com"),
            isEmailValid = true,
            errorMessage = "A user with that email was not found",
        )
    }
}
