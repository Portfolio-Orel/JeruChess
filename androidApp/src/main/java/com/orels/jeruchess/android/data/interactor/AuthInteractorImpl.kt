package com.orels.jeruchess.android.data.interactor

import android.app.Activity
import android.content.Context
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.main.domain.data.users.UsersClient
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.orels.jeruchess.main.domain.model.User
import com.orels.jeruchess.utils.PasswordGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

enum class AuthState {
    LOGGED_IN,
    LOGGED_OUT,
    REGISTRATION_REQUIRED,
    LOADING,
    ERROR;

    companion object {
        fun fromString(value: String): AuthState {
            return values().first { it.name == value }
        }
    }
}

class AuthInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val usersDataSource: UsersDataSource,
    private val usersClient: UsersClient,
) : AuthInteractor {
    private val authStateDataStoreName: String
        get() = "auth_state_data_store"

    private val authStatePreferencesKey = stringPreferencesKey("auth_state")

    private val Context.authStateDataStore by preferencesDataStore(name = authStateDataStoreName)
    companion object {
        var isConfigured: Boolean = false
    }

    private suspend fun setAuthState(authState: AuthState) {
        context.authStateDataStore.edit { preferences ->
            preferences[authStatePreferencesKey] = authState.name
        }
    }

    override suspend fun initialize(configFile: ConfigFile) {
        if (isConfigured) return
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        Amplify.configure(
            AmplifyConfiguration.fromConfigFile(context, configFile.fileResId),
            context
        )
        isConfigured = true
        if (!isUserLoggedIn()) {
            usersDataSource.clearUser()
        }
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
    }

    override suspend fun isUserLoggedIn(): Boolean =
        try {
            val authSession = Amplify.Auth.fetchAuthSession()
            authSession.isSignedIn
        } catch (e: Exception) {
            false
        }


    override suspend fun isUserRegistered(userId: String): Boolean {
        val user = usersClient.getUser(userId)
        return user != null
    }

    override suspend fun saveUser(user: User) = usersDataSource.saveUser(user)

    override suspend fun getCurrentUserEmail(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getToken(): String =
        (Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession).userPoolTokensResult.value?.accessToken
            ?: ""

    override suspend fun getUserId(): String = Amplify.Auth.getCurrentUser().userId


    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun getUserFlow(): CommonFlow<User?> = usersDataSource.getUserFlow()
    override suspend fun getAuthState(): CommonFlow<AuthState> = context.authStateDataStore.data.map {
        AuthState.fromString(it[authStatePreferencesKey] ?: AuthState.LOGGED_OUT.name)
    }.toCommonFlow()


    override suspend fun loginWithGoogle(activity: Activity): User? {
        return try {
            Amplify.Auth.signOut()
            Amplify.Auth.signInWithSocialWebUI(
                AuthProvider.google(),
                activity
            )
            Amplify.Auth.resendUserAttributeConfirmationCode(
                AuthUserAttributeKey.email()
            )
            return buildUser()
        } catch (error: AmplifyException) {
            when (error) {
                is SignedInException -> buildUser()
                else -> Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
            }
            null
        }
    }

    private suspend fun setUser() {
        val user = buildUser()
        user?.let { usersDataSource.saveUser(it) }
    }

    private suspend fun buildUser(): User? {
        val userId = Amplify.Auth.getCurrentUser().userId
        val token =
            (Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession).userPoolTokensResult.value?.accessToken
        if (token == null) {
            logout()
            return null
        }
        return User(
            id = userId,
            token = token
        )
    }
}