package com.pronoidsoftware.tasky

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.core.domain.SessionStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionStorage: SessionStorage,
    val clock: Clock,
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = sessionStorage.get() != null,
            )
            state = state.copy(isCheckingAuth = false)
        }
    }
}
