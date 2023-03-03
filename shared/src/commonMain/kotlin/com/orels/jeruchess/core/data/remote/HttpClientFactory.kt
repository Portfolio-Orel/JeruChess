package com.orels.jeruchess.core.data.remote

import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import io.ktor.client.*

expect class HttpClientFactory {
    fun create(
        baseUrl: String,
        dataSource: UsersDataSource
    ): HttpClient
}