package com.orels.jeruchess.authentication.data

import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.domain.exceptions.InvalidUserException
import com.orels.jeruchess.authentication.domain.model.User
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.auth

class KtorAuthClient(
) : AuthClient {

    private val firebase = Firebase.auth

    override suspend fun login(username: String, password: String): User? {
        try {
            val result = firebase.signInWithEmailAndPassword(username, password)
            return if (result.user != null && result.user?.email != null && result.user?.uid != null) {
                User(
                    id = result.user!!.uid,
                    email = result.user!!.email!!,
                    isGuest = false
                )
            } else {
                null
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> throw InvalidUserException
                else -> throw e
            }
        }
    }


}
