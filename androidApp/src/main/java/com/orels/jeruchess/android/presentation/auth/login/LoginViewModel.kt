package com.orels.jeruchess.android.presentation.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.domain.AuthEvent
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.android.domain.AuthState
import com.orels.jeruchess.android.domain.exceptions.UserNotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

    private fun loginWithEmail(email: String) {
        state = state.copy(isLoadingLogin = true)
        val loginJob = viewModelScope.async {
            authInteractor.onAuth(AuthEvent.LoginWithEmail(email))
            state = state.copy(isLoadingLogin = false)
        }
        viewModelScope.launch {
            try {
                loginJob.await()
            } catch (e: Exception) {
                state = when(e) {
                    is UserNotFoundException -> state.copy(error = R.string.user_not_found, isLoadingLogin = false)
                    else -> state.copy(isLoadingLogin = false)
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authInteractor.onAuth(AuthEvent.Logout)
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> loginWithEmail(event.email)
            is LoginEvent.Register -> state =
                state.copy(authState = AuthState.RegistrationRequired())
            is LoginEvent.Logout -> logout()
        }
    }
}