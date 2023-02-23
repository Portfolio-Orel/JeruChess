package com.orels.jeruchess.android.presentation.auth.login

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : ViewModel() {

    var state by mutableStateOf(LoginState())

    private fun loginWithGoogle(activity: Activity) {
        viewModelScope.launch {
            authInteractor.loginWithGoogle(activity)
        }
    }

    private fun loginWithPhone(phoneNumber: String) {
        state = state.copy(isLoadingLogin = true)
        viewModelScope.launch {
            if (phoneNumber.length < 8) {
                authInteractor.register(phoneNumber, "")
            } else {
                authInteractor.loginWithPhone(phoneNumber)
            }
            state = state.copy(isLoadingLogin = false)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authInteractor.logout()
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginWithGoogle -> loginWithGoogle(event.activity)
            is LoginEvent.Login -> loginWithPhone(event.phoneNumber)
            is LoginEvent.Register -> state =
                state.copy(authState = AuthState.REGISTRATION_REQUIRED)
            is LoginEvent.Logout -> logout()
        }
    }
}