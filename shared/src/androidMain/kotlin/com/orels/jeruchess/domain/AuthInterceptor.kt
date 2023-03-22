package com.orels.jeruchess.domain

data class AuthHeader(
    val userId: String,
    val token: String
)

interface AuthInterceptor {
    suspend fun getAuthHeader(): AuthHeader
}