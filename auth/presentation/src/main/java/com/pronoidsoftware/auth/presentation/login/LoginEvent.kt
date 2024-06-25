package com.pronoidsoftware.auth.presentation.login

import com.pronoidsoftware.core.presentation.ui.UiText

sealed interface LoginEvent {
    data class Error(val error: UiText) : LoginEvent
    data object LoginSuccess : LoginEvent
}
