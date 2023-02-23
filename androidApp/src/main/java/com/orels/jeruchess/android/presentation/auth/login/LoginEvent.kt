package com.orels.jeruchess.android.presentation.auth.login

import android.app.Activity

sealed class LoginEvent {
    data class Login(val phoneNumber: String, val activity: Activity) : LoginEvent()
    data class LoginWithGoogle(val activity: Activity) : LoginEvent()
    object Register : LoginEvent()
    object Logout : LoginEvent()
}