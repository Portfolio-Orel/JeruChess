package com.orels.jeruchess.android.presentation.auth.login

sealed class LoginEvent {
    data class Login(val email: String) : LoginEvent()
    object Register : LoginEvent()
    object Logout : LoginEvent()
}