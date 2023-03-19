package com.orels.jeruchess.core.data.remote

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
            install(ContentNegotiation) {
                json()
            }
        }
        client.plugin(HttpSend).intercept { request ->
            dataSource.getUser()?.id?.let {
                request.headers.append("userid", it)
                request.headers.append("Content-Type", "application/json")
            }
            val originalCall = execute(request)
            if (originalCall.response.status.value !in 100..399) {
                execute(request)
            } else {
                originalCall
            }
        }
        return client
    }
}