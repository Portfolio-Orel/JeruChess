package com.orels.jeruchess.main.domain.data.users

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.User

interface UsersDataSource {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun updateUser(user: User)
    suspend fun getUserFlow(): CommonFlow<User?>
    suspend fun clearUser()
}