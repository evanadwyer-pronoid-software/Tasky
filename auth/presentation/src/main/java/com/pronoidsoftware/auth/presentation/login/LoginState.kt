package com.pronoidsoftware.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf

data class LoginState(
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isLoggingIn: Boolean = false,
) {
    val canLogin = derivedStateOf {
        !isLoggingIn && isEmailValid && password.text.isNotBlank()
    }

    private infix fun TextFieldState.equals(other: TextFieldState): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false
        if (text != other.text) return false

        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginState

        if (!(email equals other.email)) return false
        if (isEmailValid != other.isEmailValid) return false
        if (!(password equals other.password)) return false
        if (isPasswordVisible != other.isPasswordVisible) return false
        if (isLoggingIn != other.isLoggingIn) return false
        if (canLogin.value != other.canLogin.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + isEmailValid.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + isPasswordVisible.hashCode()
        result = 31 * result + isLoggingIn.hashCode()
        result = 31 * result + canLogin.hashCode()
        return result
    }
}
