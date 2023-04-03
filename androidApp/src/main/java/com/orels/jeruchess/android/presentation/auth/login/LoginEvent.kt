package com.orels.jeruchess.android.presentation.auth.login

sealed class LoginEvent {
    data class Login(val phoneNumber: String) : LoginEvent()
    object Register : LoginEvent()
    object Logout : LoginEvent()
}