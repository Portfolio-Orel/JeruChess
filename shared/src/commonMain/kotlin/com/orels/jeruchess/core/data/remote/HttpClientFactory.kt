package com.orels.jeruchess.core.data.remote

import io.ktor.client.*

expect class HttpClientFactory {
    fun create(baseUrl: String = ""): HttpClient
}