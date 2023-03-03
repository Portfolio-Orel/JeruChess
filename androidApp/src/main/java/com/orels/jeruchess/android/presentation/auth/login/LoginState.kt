package com.orels.jeruchess.android.presentation.auth.login

import com.orels.jeruchess.android.data.interactor.AuthState
import com.orels.jeruchess.main.domain.model.User

data class LoginState(
    val user: User = User(
        id = "",
        phoneNumber = "0543056286",
        email = "orelsmail@gmail.com"
    ),
    val isLoading: Boolean = false,
    val isLoadingLogin: Boolean = false,
    val authState: AuthState = AuthState.LOGGED_OUT,
    val error: String? = null
)