package com.pronoidsoftware.auth.presentation.register

import com.pronoidsoftware.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess : RegisterEvent
    data class Error(val error: UiText) : RegisterEvent
}
