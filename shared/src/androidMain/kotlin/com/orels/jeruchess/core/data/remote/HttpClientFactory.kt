package com.orels.jeruchess.core.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

actual class HttpClientFactory {
    actual fun create(baseUrl: String): HttpClient {
       return HttpClient(Android) {
           defaultRequest {
                host = baseUrl
           }
           install(ContentNegotiation) {
                json()
           }
       }
    }
}