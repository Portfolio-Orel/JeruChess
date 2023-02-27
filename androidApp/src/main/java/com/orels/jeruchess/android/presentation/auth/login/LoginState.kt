package com.orels.jeruchess.android.presentation.auth.login

import com.orels.jeruchess.main.domain.model.User

data class LoginState(
    val user: User = User(
        id = "",
        phoneNumber = "0543056286",
        email = "orelsmail@gmail.com"
    ),
    val isLoading: Boolean = false,
    val isLoadingLogin: Boolean = false,
    val authState: AuthState = AuthState.DEFAULT,
    val error: String? = null
)

enum class AuthState {
    REGISTRATION_REQUIRED,
    DEFAULT,
}