package com.orels.jeruchess.android.presentation.login

import com.orels.jeruchess.core.domain.User

abstract class LoginEvents {
    data class Login(val username: String, val password: String) : LoginEvents()
    object Register : LoginEvents()
}