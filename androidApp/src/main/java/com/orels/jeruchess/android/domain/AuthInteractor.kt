package com.orels.jeruchess.android.domain

import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.User
import kotlinx.serialization.Serializable

interface AuthInteractor {
    suspend fun initialize(configFile: ConfigFile)
    suspend fun onAuth(authEvent: AuthEvent)
    suspend fun isUserLoggedIn(): Boolean
    suspend fun isUserRegistered(userId: String): Boolean
    suspend fun getUser(): User?
    suspend fun getAuthState(): CommonFlow<AuthState>
}

@Serializable
sealed class AuthState(val name: String) {
    open fun setAttributes(attributes: Map<String, String>): AuthState {
        return this
    }

    @Serializable
    object LoggedIn : AuthState(name = "LoggedIn")

    @Serializable
    object LoggedOut : AuthState(name = "LoggedOut")

    @Serializable
    data class ConfirmationRequired(val email: String? = null, val phoneNumber: String? = null) :
        AuthState(name = "ConfirmationRequired") {
        override fun setAttributes(attributes: Map<String, String>): AuthState {
            return ConfirmationRequired(
                email = attributes["email"] ?: email,
                phoneNumber = attributes["phone_number"] ?: phoneNumber
            )
        }
    }

    @Serializable
    data class RegistrationRequired(val email: String = "", val phoneNumber: String = "") :
        AuthState(name = "RegistrationRequired") {
        override fun setAttributes(attributes: Map<String, String>): AuthState {
            return RegistrationRequired(
                email = attributes["email"] ?: email,
                phoneNumber = attributes["phone_number"] ?: phoneNumber
            )
        }
    }

    @Serializable
    object Loading : AuthState(name = "Loading")

    @Serializable
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
    data class LoginWithPhone(val phoneNumber: String) : AuthEvent()
    data class Register(val user: User) : AuthEvent()
    data class ConfirmSignUp(val user: User, val code: String) : AuthEvent()
    data class ConfirmSignIn(val code: String) : AuthEvent()
    data class CompleteRegistration(val user: User) : AuthEvent()
    data class UpdateUser(val user: User) : AuthEvent()
    object Logout : AuthEvent()
}