package com.orels.jeruchess.authentication.domain.client

import com.orels.jeruchess.authentication.domain.model.User

interface AuthClient {
    suspend fun login(username: String, password: String): User?
}