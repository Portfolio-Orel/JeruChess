package com.orels.jeruchess.authentication.domain.dataSource

import com.orels.jeruchess.authentication.domain.model.User
import com.orels.jeruchess.core.util.CommonFlow


interface AuthDataSource {
    fun getUser(): CommonFlow<User?>
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser()
}