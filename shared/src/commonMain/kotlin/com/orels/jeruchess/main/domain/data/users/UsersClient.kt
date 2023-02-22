package com.orels.jeruchess.main.domain.data.users

import com.orels.jeruchess.main.domain.model.User

interface UsersClient {
    suspend fun createUser(user: User): User
    suspend fun getUser(userId: String): User?
}