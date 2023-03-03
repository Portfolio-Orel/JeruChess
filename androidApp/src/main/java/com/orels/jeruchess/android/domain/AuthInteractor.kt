package com.orels.jeruchess.android.domain

import android.app.Activity
import com.orels.jeruchess.android.data.interactor.AuthState
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.User

interface AuthInteractor {
    suspend fun initialize(configFile: ConfigFile)
    suspend fun login(email: String, password: String)
    suspend fun loginWithGoogle(activity: Activity): User?
    suspend fun loginWithPhone(phoneNumber: String)
    suspend fun register(user: User)
    suspend fun logout()
    suspend fun isUserLoggedIn(): Boolean
    suspend fun isUserRegistered(userId: String): Boolean
    suspend fun saveUser(user: User)
    suspend fun getCurrentUserEmail(): String
    suspend fun getToken(): String
    suspend fun getUserId(): String
    suspend fun getUser(): User
    suspend fun getUserFlow(): CommonFlow<User?>

    suspend fun getAuthState(): CommonFlow<AuthState>
}