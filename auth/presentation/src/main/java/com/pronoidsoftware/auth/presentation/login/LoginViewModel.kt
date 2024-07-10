package com.pronoidsoftware.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.auth.domain.AuthRepository
import com.pronoidsoftware.auth.domain.UserDataValidator
import com.pronoidsoftware.auth.presentation.R
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.onError
import com.pronoidsoftware.core.domain.util.onSuccess
import com.pronoidsoftware.core.presentation.ui.UiText
import com.pronoidsoftware.core.presentation.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        snapshotFlow { state.email.text }
            .onEach { email ->
                state = state.copy(
                    isEmailValid = userDataValidator.validateEmail(email.toString()),
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible,
                )
            }

            else -> {
                Timber.wtf("Unknown Login Action in VM")
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)
            val result = authRepository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString().trim(),
            )
            state = state.copy(isLoggingIn = false)

            result
                .onError { error ->
                    if (error == DataError.Network.UNAUTHORIZED) {
                        eventChannel.send(
                            LoginEvent.Error(
                                UiText.StringResource(R.string.error_email_password_incorrect),
                            ),
                        )
                    } else {
                        eventChannel.send(
                            LoginEvent.Error(
                                error.asUiText(),
                            ),
                        )
                    }
                }
                .onSuccess {
                    eventChannel.send(
                        LoginEvent.LoginSuccess,
                    )
                }
        }
    }
}
