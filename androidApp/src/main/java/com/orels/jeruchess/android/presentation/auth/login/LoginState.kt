package com.orels.jeruchess.android.presentation.auth.login

import com.orels.jeruchess.authentication.domain.model.User

data class LoginState(
    val user: User = User(
        phoneNumber = "0543056286",
        email = "orelsmail@gmail.com"
    ),
    val isLoading: Boolean = false,
    val isLoadingLogin: Boolean = false,
    val authState: AuthState = AuthState.UNAUTHENTICATED,
    val error: String? = null
)

enum class AuthState {
    AUTHENTICATED,
    REGISTRATION_REQUIRED,
    UNAUTHENTICATED,
}