@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.auth.presentation.R
import com.pronoidsoftware.auth.presentation.components.AuthToolbar
import com.pronoidsoftware.core.presentation.designsystem.CheckIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray2
import com.pronoidsoftware.core.presentation.designsystem.TaskyPurple
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyActionButton
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyPasswordTextField
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyTextField
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG,
                ).show()
            }

            LoginEvent.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.login_successful,
                    Toast.LENGTH_LONG,
                ).show()
                onLoginSuccess()
            }
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is LoginAction.OnRegisterClick -> onRegisterClick()
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
internal fun LoginScreen(state: LoginState, onAction: (LoginAction) -> Unit) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
    )
    val spacing = LocalSpacing.current
    TaskyScaffold(
        topAppBar = {
            AuthToolbar(
                title = stringResource(id = R.string.welcome_back),
                scrollBehavior = scrollBehavior,
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
                        topStart = spacing.scaffoldContainerRadius,
                        topEnd = spacing.scaffoldContainerRadius,
                    ),
                )
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        topStart = spacing.scaffoldContainerRadius,
                        topEnd = spacing.scaffoldContainerRadius,
                    ),
                )
                .padding(top = spacing.scaffoldPaddingTop)
                .padding(horizontal = spacing.spaceMedium),
        ) {
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
                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                },
                hint = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(spacing.loginActionButtonSpacingTop))
            TaskyActionButton(
                text = stringResource(id = R.string.login),
                isLoading = state.isLoggingIn,
                isLoadingContentDescription = stringResource(id = R.string.logging_in),
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(LoginAction.OnLoginClick) },
                enabled = state.canLogin,
            )

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.labelMedium
                        .copy(color = TaskyGray2)
                        .toSpanStyle(),
                ) {
                    append(stringResource(id = R.string.dont_have_an_account) + " ")
                    pushStringAnnotation(
                        tag = "clickable_text",
                        annotation = stringResource(id = R.string.sign_up),
                    )
                    withStyle(
                        style = MaterialTheme.typography.labelMedium
                            .copy(color = TaskyPurple)
                            .toSpanStyle(),
                    ) {
                        append(stringResource(id = R.string.sign_up))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(spacing.spaceLarge)
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                val onClick: (Int) -> Unit = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text",
                        start = offset,
                        end = offset,
                    ).firstOrNull()?.let {
                        onAction(LoginAction.OnRegisterClick)
                    }
                }
                val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
                val pressIndicator = Modifier.pointerInput(onClick) {
                    detectTapGestures { pos ->
                        layoutResult.value?.let { layoutResult ->
                            onClick(layoutResult.getOffsetForPosition(pos))
                        }
                    }
                }

                BasicText(
                    text = annotatedString,
                    modifier = Modifier.then(pressIndicator),
                    style = TextStyle.Default,
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    maxLines = Int.MAX_VALUE,
                    onTextLayout = {
                        layoutResult.value = it
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    TaskyTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
        )
    }
}
