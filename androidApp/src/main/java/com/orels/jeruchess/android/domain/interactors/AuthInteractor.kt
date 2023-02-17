package com.orels.jeruchess.android.domain.interactors

import android.app.Activity
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.authentication.domain.model.User

interface AuthInteractor {
    suspend fun initialize(configFile: ConfigFile)

    suspend fun login(email: String, password: String, activity: Activity)
    suspend fun register(email: String, password: String)
    suspend fun logout()
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getCurrentUserEmail(): String
    suspend fun getUser(): User
}