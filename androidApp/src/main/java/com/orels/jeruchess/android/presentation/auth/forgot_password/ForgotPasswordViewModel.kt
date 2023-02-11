package com.orels.jeruchess.android.presentation.auth.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(ForgotPasswordState())

    fun onForgotPassword(username: String) {
        state = state.copy(state = State.ForgotPassword(true))
        viewModelScope.launch {
            delay(300)
            state = state.copy(state = State.ResetPassword(false))
        }
    }

    fun onResetPassword(code: String, password: String, confirmPassword: String) {
        state = state.copy(state = State.ResetPassword(true))
        viewModelScope.launch {
            delay(300)
            state = state.copy(state = State.Done)
        }
    }
}