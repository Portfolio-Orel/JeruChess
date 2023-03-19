package com.orels.jeruchess.android.presentation.auth.login

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.android.domain.AuthEvent
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.android.domain.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : ViewModel() {

    var state by mutableStateOf(LoginState())

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authInteractor.getAuthState()
                .collect {
                    state = state.copy(authState = it)
                }
        }
    }

    private fun loginWithGoogle(activity: Activity) {
        viewModelScope.launch {
            authInteractor.onAuth(AuthEvent.LoginWithGoogle(activity))
        }
    }

    private fun loginWithPhone(phoneNumber: String) {
        state = state.copy(isLoadingLogin = true)
        viewModelScope.launch {
            authInteractor.onAuth(AuthEvent.LoginWithPhone(phoneNumber))
            state = state.copy(isLoadingLogin = false)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authInteractor.onAuth(AuthEvent.Logout)
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginWithGoogle -> loginWithGoogle(event.activity)
            is LoginEvent.Login -> loginWithPhone(event.phoneNumber)
            is LoginEvent.Register -> state =
                state.copy(authState = AuthState.RegistrationRequired())
            is LoginEvent.Logout -> logout()
        }
    }
}