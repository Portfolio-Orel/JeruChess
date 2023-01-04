package com.orels.jeruchess.android.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.orels.jeruchess.core.domain.User
import com.orels.jeruchess.core.util.toCommonStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.Login -> {
                _state.update { it.copy(isLoading = true) }
            }
            is LoginEvents.Register -> {
                _state.update { it.copy(isLoading = true) }
            }
        }
    }

    fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }
}