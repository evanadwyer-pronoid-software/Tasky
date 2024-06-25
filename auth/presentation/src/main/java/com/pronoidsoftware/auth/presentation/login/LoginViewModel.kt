package com.pronoidsoftware.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.auth.domain.UserDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator,
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
            LoginAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible,
                )
            }

            else -> Timber.wtf("Unknown Login Action in VM")
        }
    }
}
