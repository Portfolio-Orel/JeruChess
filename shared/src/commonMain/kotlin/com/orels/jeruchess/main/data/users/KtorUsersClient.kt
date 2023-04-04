package com.orels.jeruchess.main.data.users

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.users.UsersClient
import com.orels.jeruchess.main.domain.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class KtorUsersClient(
    private val httpClient: HttpClient,
) : UsersClient {
    private val baseUrl = "users"
    private val json = Json {
        ignoreUnknownKeys = true
    }
    override suspend fun createUser(user: User ,userId: String): User {
        try {
            val result = httpClient.post {
                url("${NetworkConstants.BASE_URL}/$baseUrl")
                header("Content-Type", "application/json")
                header("userid", userId)
                setBody(user.toUserDto())
            }

         return json.decodeFromString<UserDto>(result.body()).toUser()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(userId: String): User? {
        return try {
            val result = httpClient.get {
                url("${NetworkConstants.BASE_URL}/$baseUrl/$userId")
                header("userid", userId)
            }
            result.body<UserDto?>()?.toUser()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateUser(user: User) {
        httpClient.put {
            url("${NetworkConstants.BASE_URL}/$baseUrl/${user.id}")
            header("Content-Type", "application/json")
            setBody(user.toUserDto())
        }
    }

    override suspend fun completeRegistration(user: User) {
        httpClient.put {
            url("${NetworkConstants.BASE_URL}/$baseUrl/${user.id}/completeRegistration")
            header("Content-Type", "application/json")
            setBody(user.toUserDto())
        }
    }

    override suspend fun isUserRegistered(userId: String): Boolean {
        return try {
            val result = httpClient.get {
                url("${NetworkConstants.BASE_URL}/$baseUrl/$userId/isRegistrationCompleted")
                header("userid", userId)
            }
            result.body<IsUserRegisteredDto?>()?.isRegistered ?: false
        } catch (e: Exception) {
            false
        }
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