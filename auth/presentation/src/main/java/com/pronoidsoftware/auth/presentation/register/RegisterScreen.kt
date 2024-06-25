@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.auth.presentation.R
import com.pronoidsoftware.auth.presentation.components.AuthToolbar
import com.pronoidsoftware.core.presentation.designsystem.BackChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.CheckIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyActionButton
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyFloatingActionButton
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyPasswordTextField
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyTextField
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents

@Composable
fun RegisterScreenRoot(
    onLogInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is RegisterEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG,
                ).show()
            }

            RegisterEvent.RegistrationSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.registration_successful,
                    Toast.LENGTH_LONG,
                ).show()
                onSuccessfulRegistration()
            }
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is RegisterAction.OnLoginClick -> onLogInClick()
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
internal fun RegisterScreen(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
    )
    val spacing = LocalSpacing.current
    TaskyScaffold(
        topAppBar = {
            AuthToolbar(
                title = stringResource(id = R.string.create_account),
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            TaskyFloatingActionButton(
                icon = BackChevronIcon,
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .clip(
                    RoundedCornerShape(
                        topStart = spacing.authContainerRadius,
                        topEnd = spacing.authContainerRadius,
                    ),
                )
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        topStart = spacing.authContainerRadius,
                        topEnd = spacing.authContainerRadius,
                    ),
                )
                .padding(top = spacing.authPaddingTop)
                .padding(horizontal = spacing.spaceMedium),
        ) {
            TaskyTextField(
                state = state.name,
                hint = stringResource(id = R.string.name),
                endIcon = if (state.isNameValid) {
                    CheckIcon
                } else {
                    null
                },
                endIconContentDescription = if (state.isNameValid) {
                    stringResource(id = R.string.name_is_valid)
                } else {
                    null
                },
                error = state.name.text.isNotBlank() && !state.isNameValid,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(spacing.authPaddingInterior))
            TaskyTextField(
                state = state.email,
                hint = stringResource(id = R.string.email),
                endIcon = if (state.isEmailValid) {
                    CheckIcon
                } else {
                    null
                },
                endIconContentDescription = if (state.isEmailValid) {
                    stringResource(id = R.string.email_is_valid)
                } else {
                    null
                },
                error = state.email.text.isNotBlank() && !state.isEmailValid,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(spacing.authPaddingInterior))
            TaskyPasswordTextField(
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                hint = stringResource(id = R.string.password),
                passwordContentDescription = if (state.passwordValidationState.isPasswordValid) {
                    stringResource(id = R.string.password_is_valid)
                } else {
                    null
                },
                error = state.password.text.isNotBlank() &&
                    !state.passwordValidationState.isPasswordValid,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(spacing.registerActionButtonSpacingTop))
            TaskyActionButton(
                text = stringResource(id = R.string.get_started),
                isLoading = state.isRegistering,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(RegisterAction.OnRegisterClick) },
                enabled = state.canRegister,
            )
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    TaskyTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
        )
    }
}
