package com.orels.jeruchess.core.data.remote

import com.orels.jeruchess.core.domain.AuthInteractor
import io.ktor.client.*

actual class HttpClientFactory {
    actual fun create(
        baseUrl: String,
        authInteractor: AuthInteractor
    ): HttpClient {
        TODO("Not yet implemented")
    }
}