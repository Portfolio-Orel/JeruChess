package com.orels.jeruchess.android.presentation.auth.login

import com.orels.jeruchess.android.domain.AuthState
import com.orels.jeruchess.main.domain.model.User

data class LoginState(
    val user: User = User(
        id = "",
        phoneNumber = "",
        email = ""
    ),
    val authState: AuthState = AuthState.LoggedOut,
    val isLoading: Boolean = false,
    val isLoadingLogin: Boolean = false,
    val error: String? = null
)