package com.orels.jeruchess.android.domain.interactors

import android.app.Activity
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.User

interface AuthInteractor {
    suspend fun loginWithGoogle(activity: Activity)
    suspend fun loginWithPhone(phoneNumber: String, activity: Activity)
    suspend fun register(user: User)
    suspend fun logout()
    suspend fun isUserRegistered(email: String?, phoneNumber: String?): Boolean
    suspend fun getUser(): User
    suspend fun getUserFlow(): CommonFlow<User>
}