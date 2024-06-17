@file:OptIn(ExperimentalFoundationApi::class)

package com.pronoidsoftware.auth.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
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
    val canRegister: Boolean = !isRegistering && isNameValid && isEmailValid && passwordValidationState.isPasswordValid,
)
