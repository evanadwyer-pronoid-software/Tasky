@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.edittext

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.agenda.presentation.edittext.components.BodyHintTextField
import com.pronoidsoftware.agenda.presentation.edittext.components.EditTextToolbar
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun EditTextScreenRoot(
    title: String,
    actionTitle: String,
    onBackClick: () -> Unit,
    onSaveClick: (String) -> Unit,
    viewModel: EditTextViewModel = hiltViewModel(),
    initialValue: String = "",
) {
    EditTextScreen(
        title = title,
        actionTitle = actionTitle,
        state = viewModel.state.copy(
            text = TextFieldState(initialText = initialValue),
        ),
        onAction = { action ->
            when (action) {
                EditTextAction.OnBackClick -> {
                    onBackClick()
                }

                EditTextAction.OnSaveClick -> {
                    onSaveClick(viewModel.state.text.text.toString())
                }

                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
internal fun EditTextScreen(
    title: String,
    actionTitle: String,
    state: EditTextState,
    onAction: (EditTextAction) -> Unit,
) {
    Scaffold(
        topBar = {
            EditTextToolbar(
                title = title,
                actionText = actionTitle,
                onBackClick = {
                    onAction(EditTextAction.OnBackClick)
                },
                onSaveClick = { onAction(EditTextAction.OnSaveClick) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 17.dp),
        ) {
            HorizontalDivider(
                color = TaskyLightGray,
                thickness = 2.dp,
            )
            Spacer(modifier = Modifier.height(33.dp))
            BodyHintTextField(
                state = state.text,
                hint = state.hint,
                textStyle = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.W400,
                    fontSize = 26.sp,
                    lineHeight = 12.sp,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun EditTextScreenPreview() {
    TaskyTheme {
        EditTextScreen(
            title = "EDIT DESCRIPTION",
            actionTitle = "Save",
            state = EditTextState(
                title = "EDIT TITLE",
                action = "Save",
                hint = "Title",
            ),
            onAction = {},
        )
    }
}
