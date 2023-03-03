package com.orels.jeruchess.domain

interface AuthInterceptor {
    suspend fun getAuthHeader(): String
}