package com.orels.jeruchess.authentication.domain.client

interface AuthClient {
    suspend fun login(username: String, password: String)
}