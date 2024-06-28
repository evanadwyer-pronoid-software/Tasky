package com.pronoidsoftware.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Preview
@Composable
private fun LoginScreenPreview_Empty() {
    TaskyTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview_AllValid_PasswordHidden() {
    TaskyTheme {
        LoginScreen(
            state = loginStateAllValid(),
            onAction = {},
        )
    }
//    showIme()
}

@Preview
@Composable
private fun LoginScreenPreview_AllValid_PasswordVisible() {
    TaskyTheme {
        LoginScreen(
            state = loginStateAllValid().copy(
                isPasswordVisible = true,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview_LoggingIn() {
    TaskyTheme {
        LoginScreen(
            state = loginStateAllValid().copy(
                isLoggingIn = true,
            ),
            onAction = {},
        )
    }
}

private fun loginStateAllValid() = LoginState(
    email = TextFieldState("j.doe@example.com"),
    isEmailValid = true,
    password = TextFieldState("Test12345"),
)
