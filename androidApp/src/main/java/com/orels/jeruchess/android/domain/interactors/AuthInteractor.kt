package com.orels.jeruchess.android.domain.interactors

import com.orels.jeruchess.core.domain.User

interface AuthInteractor {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getCurrentUserEmail(): String
    suspend fun getUser(): User
}