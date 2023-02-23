package com.orels.jeruchess.android.domain.interactors

import android.app.Activity
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.main.domain.model.User

interface AuthInteractor {
    fun initialize(configFile: ConfigFile)
    suspend fun login(email: String, password: String)
    suspend fun loginWithGoogle(activity: Activity)
    suspend fun loginWithPhone(phoneNumber: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
    suspend fun isUserLoggedIn(): Boolean
    suspend fun isUserRegistered(email: String?, phoneNumber: String?): Boolean
    suspend fun getCurrentUserEmail(): String
    suspend fun getUser(): User
}