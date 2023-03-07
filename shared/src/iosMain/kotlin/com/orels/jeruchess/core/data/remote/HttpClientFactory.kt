package com.orels.jeruchess.core.data.remote

import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

actual class HttpClientFactory {
    actual fun create(
        baseUrl: String,
        dataSource: UsersDataSource
    ): HttpClient {
        return HttpClient(Darwin) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}