package com.orels.jeruchess.android.domain

import android.app.Activity
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.User

interface AuthInteractor {
    suspend fun initialize(configFile: ConfigFile)
    suspend fun onAuth(authEvent: AuthEvent)
    suspend fun isUserLoggedIn(): Boolean
    suspend fun isUserRegistered(userId: String): Boolean
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun getAuthState(): CommonFlow<AuthState>
}

sealed class AuthState(val name: String) {
    object LoggedIn : AuthState(name = "LoggedIn")
    object LoggedOut : AuthState(name = "LoggedOut")
    data class ConfirmationRequired(val email: String? = null, val phoneNumber: String? = null) :
        AuthState(name = "ConfirmationRequired")

    data class RegistrationRequired(val email: String = "", val phoneNumber: String = "") :
        AuthState(name = "RegistrationRequired")

    object Loading : AuthState(name = "Loading")
    data class Error(val message: String) : AuthState(name = "Error")

    companion object {
        fun fromString(name: String): AuthState = when (name) {
            LoggedIn.name -> LoggedIn
            LoggedOut.name -> LoggedOut
            ConfirmationRequired().name -> ConfirmationRequired()
            RegistrationRequired().name -> RegistrationRequired()
            Loading.name -> Loading
            else -> Error(name)
        }
    }
}

sealed class AuthEvent {
    data class LoginWithGoogle(val activity: Activity) : AuthEvent()
    data class LoginWithPhone(val phoneNumber: String) : AuthEvent()
    data class Register(val user: User) : AuthEvent()
    object Logout : AuthEvent()
}