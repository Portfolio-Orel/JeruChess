package com.orels.jeruchess.android.data.interactor

import android.app.Activity
import android.content.Context
import android.telephony.PhoneNumberUtils
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.exceptions.invalidstate.SignedInException
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignUpOptions
import com.amplifyframework.auth.cognito.options.AuthFlowType
import com.amplifyframework.core.AmplifyConfiguration
import com.amplifyframework.kotlin.core.Amplify
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.data.users.UsersClient
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.orels.jeruchess.main.domain.model.User
import com.orels.jeruchess.utils.PasswordGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val usersDataSource: UsersDataSource,
    private val usersClient: UsersClient,
) : AuthInteractor {

    companion object {
        var isConfigured: Boolean = false
    }

    override fun initialize(configFile: ConfigFile) {
        if (isConfigured) return
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        Amplify.configure(
            AmplifyConfiguration.fromConfigFile(context, configFile.fileResId),
            context
        )
        isConfigured = true
    }

    override suspend fun login(email: String, password: String) {

//        val result = auth.signInWithEmailAndPassword(email, password)
//        if (!result.isSuccessful) {
//            when (result.exception) {
//                is FirebaseAuthInvalidUserException -> throw InvalidUserException
//                is Exception -> throw result.exception!!
//            }
//        }
    }

    override suspend fun loginWithPhone(phoneNumber: String) {
        try {
            val options = AWSCognitoAuthSignInOptions.builder()
                .authFlowType(AuthFlowType.USER_PASSWORD_AUTH)
                .build()
            Amplify.Auth.signIn()
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    override suspend fun register(user: User) {
        val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(user.phoneNumber, "IL")
        val signUpOptions = AWSCognitoAuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.phoneNumberVerified(), formattedPhoneNumber)
            .userAttribute(AuthUserAttributeKey.phoneNumber(), formattedPhoneNumber)
            .userAttribute(AuthUserAttributeKey.email(), user.email)
            .userAttribute(AuthUserAttributeKey.emailVerified(), user.email)
            .userAttribute(AuthUserAttributeKey.birthdate(), Date(user.dateOfBirth).toString())
            .userAttribute(AuthUserAttributeKey.givenName(), user.firstName)
            .userAttribute(AuthUserAttributeKey.familyName(), user.lastName)
            .build()

        try {
            val res = Amplify.Auth.signUp(
                user.email,
                PasswordGenerator.generateStrongPassword(),
                signUpOptions
            )
            val userId = Amplify.Auth.getCurrentUser().userId
            val token =
                (Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession).userPoolTokensResult.value?.accessToken
            if (token == null) {
                logout()
                return
            }
            user.id = userId
            user.token = token
            usersClient.createUser(user)
            usersDataSource.saveUser(user)
        } catch (e: Exception) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", e)
        }
    }

    suspend fun confirm() {
        Amplify.Auth.confirmSignUp("orelsmail@gmail.com", "227314")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun isUserRegistered(email: String?, phoneNumber: String?): Boolean {
//        usersClient.getUser()
        return false
    }


    override suspend fun getCurrentUserEmail(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun getUserFlow(): CommonFlow<User?> = usersDataSource.getUserFlow()


    override suspend fun loginWithGoogle(activity: Activity) {
        try {
            Amplify.Auth.signInWithSocialWebUI(
                AuthProvider.google(),
                activity
            )
            setUser()
        } catch (error: AmplifyException) {
            when (error) {
                is SignedInException -> setUser()
                else -> Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
            }
        }
    }

    private suspend fun setUser() {
        val userId = Amplify.Auth.getCurrentUser().userId
        val token =
            (Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession).userPoolTokensResult.value?.accessToken
        if (token == null) {
            logout()
            return
        }
        val user = User(
            id = userId,
            token = token
        )
//        usersClient.createUser(user) TODO
        usersDataSource.saveUser(user)

    }
}