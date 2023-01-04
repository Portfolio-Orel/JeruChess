package com.orels.jeruchess.android.presentation.login

import androidx.annotation.StringRes

data class LoginState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",

    @StringRes val error: Int? = null
)