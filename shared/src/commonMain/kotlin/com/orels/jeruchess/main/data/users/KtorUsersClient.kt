package com.orels.jeruchess.main.data.users

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.users.UsersClient
import com.orels.jeruchess.main.domain.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class KtorUsersClient(
    private val httpClient: HttpClient,
) : UsersClient {
    private val baseUrl = NetworkConstants.BASE_URL + "/users"

    override suspend fun createUser(user: User): User {
        try {
            val result = httpClient.post {
                url(baseUrl)
                setBody(user.toUserDto())
            }
            return result.body<UserDto>().toUser()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(userId: String): User? {
        try {
            val result = httpClient.get {
                url("$baseUrl/$userId")
            }
            return result.body<UserDto>().toUser()
        } catch (e: Exception) {
            throw e
        }
    }

}