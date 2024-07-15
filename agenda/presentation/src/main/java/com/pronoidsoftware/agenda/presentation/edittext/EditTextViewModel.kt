package com.pronoidsoftware.agenda.presentation.edittext

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class EditTextViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(EditTextState())
        private set

    fun onAction(action: EditTextAction) {
        when (action) {
            EditTextAction.OnSaveClick -> {
            }

            else -> {
                Timber.wtf("Unknown EditTextAction in VM")
            }
        }
    }
}
