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

enum class AuthState {
    LOGGED_IN,
    LOGGED_OUT,
    CONFIRMATION_REQUIRED,
    REGISTRATION_REQUIRED,
    LOADING,
    ERROR;

    companion object {
        fun fromString(value: String): AuthState {
            return values().first { it.name == value }
        }
    }
}

sealed class AuthEvent {
    data class LoginWithGoogle(val activity: Activity) : AuthEvent()
    data class LoginWithPhone(val phoneNumber: String) : AuthEvent()
    data class Register(val user: User) : AuthEvent()
    object Logout : AuthEvent()
}