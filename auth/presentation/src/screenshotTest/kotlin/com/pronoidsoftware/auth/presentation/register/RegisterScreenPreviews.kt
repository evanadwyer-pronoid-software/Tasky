package com.pronoidsoftware.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.auth.domain.PasswordValidationState
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Preview
@Composable
private fun RegisterScreenPreview_Empty() {
    TaskyTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview_AllValid_PasswordHidden() {
    TaskyTheme {
        RegisterScreen(
            state = registerStateAllValid(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview_AllValid_PasswordVisible() {
    TaskyTheme {
        RegisterScreen(
            state = registerStateAllValid().copy(
                isPasswordVisible = true,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview_Registering() {
    TaskyTheme {
        RegisterScreen(
            state = registerStateAllValid().copy(
                isRegistering = true,
            ),
            onAction = {},
        )
    }
}

private fun registerStateAllValid() = RegisterState(
    name = TextFieldState("John Doe"),
    isNameValid = true,
    email = TextFieldState("j.doe@example.com"),
    isEmailValid = true,
    password = TextFieldState("Test12345"),
    passwordValidationState = PasswordValidationState(
        hasMinimumLength = true,
        hasUpperCaseCharacter = true,
        hasLowerCaseCharacter = true,
        hasDigit = true,
    ),
)
