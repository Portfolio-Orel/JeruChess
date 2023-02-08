package com.orels.jeruchess.android.presentation.login

abstract class LoginEvents {
    data class Login(val username: String, val password: String) : LoginEvents()
    data class LoginCompleted(val isAuthorized: Boolean = false, val exception: Exception? = null) : LoginEvents()
    object Register : LoginEvents()
}