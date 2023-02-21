package com.orels.jeruchess.authentication.domain.client

import com.orels.jeruchess.main.domain.model.User

interface AuthClient {
    suspend fun login(username: String, password: String): User?
    suspend fun logout()
}