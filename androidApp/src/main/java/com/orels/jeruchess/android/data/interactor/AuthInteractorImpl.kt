package com.orels.jeruchess.android.data.interactor

import android.app.Activity
import android.content.Context
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.authentication.domain.exceptions.InvalidUserException
import com.orels.jeruchess.authentication.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthInteractor {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        var isConfigured: Boolean = false
    }

    override suspend fun initialize(configFile: ConfigFile) {
        if (isConfigured) return
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        Amplify.configure(
            AmplifyConfiguration.fromConfigFile(context, configFile.fileResId),
            context
        )
        isConfigured = true
    }

    override suspend fun login(email: String, password: String, activity: Activity) {
        try {
            Amplify.Auth.signOut { Log.e("AuthQuickstart", "Sign out failed") }
            Amplify.Auth.signInWithSocialWebUI(
                AuthProvider.google(),
                activity,
                { Log.i("AuthQuickstart", "Result: $it") },
                { Log.e("AuthQuickstart", "Sign in failed", it) }
            )
            Log.i(
                "MyAmplifyApp",
                "Initialized Amplify"
            )
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
//        val result = auth.signInWithEmailAndPassword(email, password)
//        if (!result.isSuccessful) {
//            when (result.exception) {
//                is FirebaseAuthInvalidUserException -> throw InvalidUserException
//                is Exception -> throw result.exception!!
//            }
//        }
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