package com.pronoidsoftware.agenda.presentation.edittext

sealed interface EditTextAction {
    data object OnBackClick : EditTextAction
    data object OnSaveClick : EditTextAction
}
