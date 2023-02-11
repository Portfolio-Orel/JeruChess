package com.orels.jeruchess.authentication.presentation

data class AuthState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoadingLogin: Boolean = false,
    val isAuthorized: Boolean = false,
    val error: String? = null
)