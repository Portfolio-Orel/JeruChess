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
    private val baseUrl = "users"

    override suspend fun createUser(user: User): User {
        try {
            val result = httpClient.post {
                url("${NetworkConstants.BASE_URL}/$baseUrl")
                header("Content-Type", "application/json")
                setBody(user.toUserDto())
            }
            return result.body<UserDto>().toUser()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(userId: String): User? {
            val result = httpClient.get {
                url("${NetworkConstants.BASE_URL}/$baseUrl/$userId")
                header("userid", userId)
            }
            return result.body<UserDto?>()?.toUser()
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): User {
        try {
            val result = httpClient.get {
                url("$baseUrl/$phoneNumber")
            }
            return result.body<UserDto>().toUser()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        try {
            val result = httpClient.get {
                url("$baseUrl/$email")
            }
            return result.body<UserDto>().toUser()
        } catch (e: Exception) {
            throw e
        }
    }

}