package com.pronoidsoftware.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.auth.domain.AuthRepository
import com.pronoidsoftware.auth.presentation.R
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.onError
import com.pronoidsoftware.core.domain.util.onSuccess
import com.pronoidsoftware.core.domain.validation.UserDataValidator
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
class RegisterViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        snapshotFlow { state.name.text }
            .onEach { name ->
                state = state.copy(
                    isNameValid = userDataValidator.validateName(name.toString()),
                )
            }
            .launchIn(viewModelScope)

        snapshotFlow { state.email.text }
            .onEach { email ->
                state = state.copy(
                    isEmailValid = userDataValidator.validateEmail(email.toString()),
                )
            }
            .launchIn(viewModelScope)

        snapshotFlow { state.password.text }
            .onEach { password ->
                state = state.copy(
                    passwordValidationState = userDataValidator.validatePassword(
                        password.toString(),
                    ),
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnRegisterClick -> register()

            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible,
                )
            }

            else -> {
                Timber.wtf("Unknown Register Action in VM")
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)
            val result = authRepository.register(
                fullName = state.name.text.toString().trim(),
                email = state.email.text.toString().trim(),
                password = state.password.text.toString().trim(),
            )
            state = state.copy(isRegistering = false)

            result
                .onError { error ->
                    if (error == DataError.Network.CONFLICT) {
                        eventChannel.send(
                            RegisterEvent.Error(
                                UiText.StringResource(R.string.error_user_already_exists),
                            ),
                        )
                    } else {
                        eventChannel.send(RegisterEvent.Error(error.asUiText()))
                    }
                }
                .onSuccess {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
        }
    }
}
