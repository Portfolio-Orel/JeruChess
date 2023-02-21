package com.orels.jeruchess.authentication.domain.dataSource

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.User


interface AuthDataSource {
    fun getUser(): CommonFlow<User?>
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser()
}