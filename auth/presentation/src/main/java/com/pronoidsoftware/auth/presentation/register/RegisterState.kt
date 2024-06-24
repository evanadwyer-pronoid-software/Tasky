package com.pronoidsoftware.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.pronoidsoftware.auth.domain.PasswordValidationState

data class RegisterState(
    val name: TextFieldState = TextFieldState(),
    val isNameValid: Boolean = false,
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    var isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,
    val canRegister: Boolean = !isRegistering &&
        isNameValid &&
        isEmailValid &&
        passwordValidationState.isPasswordValid,
) {
    private infix fun TextFieldState.equals(other: TextFieldState): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false
        if (text != other.text) return false

        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterState

        if (!(name equals other.name)) return false
        if (isNameValid != other.isNameValid) return false
        if (!(email equals other.email)) return false
        if (isEmailValid != other.isEmailValid) return false
        if (!(password equals other.password)) return false
        if (isPasswordVisible != other.isPasswordVisible) return false
        if (passwordValidationState != other.passwordValidationState) return false
        if (isRegistering != other.isRegistering) return false
        if (canRegister != other.canRegister) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + isNameValid.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + isEmailValid.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + isPasswordVisible.hashCode()
        result = 31 * result + passwordValidationState.hashCode()
        result = 31 * result + isRegistering.hashCode()
        result = 31 * result + canRegister.hashCode()
        return result
    }
}
