package com.pronoidsoftware.agenda.presentation.edittext

import androidx.compose.foundation.text.input.TextFieldState

data class EditTextState(
    val text: TextFieldState = TextFieldState(),
    val title: String = "",
    val action: String = "",
    val hint: String = "",
)
