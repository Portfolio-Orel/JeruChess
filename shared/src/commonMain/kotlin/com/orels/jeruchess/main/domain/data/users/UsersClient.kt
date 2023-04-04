package com.orels.jeruchess.main.domain.data.users

import com.orels.jeruchess.main.domain.model.User

interface UsersClient {
    suspend fun createUser(user: User, userId: String): User
    suspend fun getUser(userId: String): User?
    suspend fun isUserRegistered(userId: String): Boolean
    suspend fun updateUser(user: User)

    suspend fun completeRegistration(user: User)
    suspend fun getUserByPhoneNumber(phoneNumber: String): User?
    suspend fun getUserByEmail(email: String): User?
}