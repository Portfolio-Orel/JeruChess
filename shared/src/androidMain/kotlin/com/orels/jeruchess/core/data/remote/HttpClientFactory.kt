package com.orels.jeruchess.core.data.remote

import com.orels.jeruchess.core.data.remote.interceptor.AuthInterceptorImpl
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

actual class HttpClientFactory {
    actual fun create(
        baseUrl: String,
        dataSource: UsersDataSource
    ): HttpClient {
        val client = HttpClient(Android) {
            defaultRequest {
                host = baseUrl
            }
            install(AuthInterceptorImpl) {
                this.dataSource = dataSource
            }
            install(ContentNegotiation) {
                json()
            }
        }

        return client
    }
}