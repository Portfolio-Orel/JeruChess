package com.orels.jeruchess.android.data.interactor

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.orels.jeruchess.authentication.domain.exceptions.InvalidUserException
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.authentication.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthInteractor {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String) {
        val result = auth.signInWithEmailAndPassword(email, password)
        if (!result.isSuccessful) {
            when (result.exception) {
                is FirebaseAuthInvalidUserException -> throw InvalidUserException
                is Exception -> throw result.exception!!
            }
        }
    }

    override suspend fun register(email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password)
        if (!result.isSuccessful) {
            when (result.exception) {
                is FirebaseAuthInvalidUserException -> throw InvalidUserException
                is Exception -> throw result.exception!!
            }
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }

    override suspend fun getUser(): User {
        return User(
            email = auth.currentUser?.email ?: "",
            id = auth.currentUser?.uid ?: "",
            firstName = auth.currentUser?.displayName ?: "",
            lastName = "",
            isGuest = auth.currentUser?.uid == ""
        )
    }
}