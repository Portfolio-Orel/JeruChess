package com.orels.jeruchess.authentication.data

import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.domain.exceptions.InvalidUserException
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.auth

class KtorAuthClient(
) : AuthClient {

    private val firebase = Firebase.auth

    override suspend fun login(username: String, password: String) {
        try {
            val result = firebase.signInWithEmailAndPassword(username, password)
            if (result.user != null) {
                println("Login successful")
            } else {
                println("Login failed")
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> throw InvalidUserException
                else -> throw e
            }
        }
    }


}
