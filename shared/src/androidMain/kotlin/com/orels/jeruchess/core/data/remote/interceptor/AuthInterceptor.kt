package com.orels.jeruchess.core.data.remote.interceptor

import com.orels.jeruchess.domain.AuthHeader
import com.orels.jeruchess.domain.AuthInterceptor
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.util.*

class AuthInterceptorImpl(
    private var dataSource: UsersDataSource?,
) : AuthInterceptor {
    override suspend fun getAuthHeader(): AuthHeader {
        val user = dataSource?.getUser()
        return AuthHeader(
            userId = user?.id ?: "",
            token = user?.token ?: ""
        )
    }

    object Config {
        var dataSource: UsersDataSource? = null
    }

    companion object : HttpClientPlugin<Config, AuthInterceptor> {
        override val key: AttributeKey<AuthInterceptor> = AttributeKey("AuthInterceptor")

        override fun prepare(block: Config.() -> Unit): AuthInterceptor {
            Config.apply(block)
            return AuthInterceptorImpl(
                dataSource = Config.dataSource,
            )
        }

        override fun install(plugin: AuthInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                val authHeader = plugin.getAuthHeader()
                this.context.headers.append("Authorization", authHeader.token)
                this.context.headers.append("userid", authHeader.userId)
                this.context.headers.append("Content-Type", "application/json")
                proceedWith(this.context)
            }
        }

    }
}