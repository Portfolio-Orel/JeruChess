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
import com.amplifyframework.auth.cognito.exceptions.service.UsernameExistsException
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignUpOptions
import com.amplifyframework.auth.cognito.options.AuthFlowType
import com.amplifyframework.core.AmplifyConfiguration
import com.amplifyframework.kotlin.core.Amplify
import com.orels.jeruchess.android.domain.AuthEvent
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.android.domain.AuthState
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.main.domain.data.users.UsersClient
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.orels.jeruchess.main.domain.model.User
import com.orels.jeruchess.utils.PasswordGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    private val authStateDataStoreName: String
        get() = "auth_state_data_store"

    private val authStatePreferencesKey = stringPreferencesKey("auth_state")
    private val authStateAttributesPreferencesKey = stringPreferencesKey("auth_state_attributes")

    private val Context.authStateDataStore by preferencesDataStore(name = authStateDataStoreName)

    private suspend fun setAuthState(
        authState: AuthState,
        attributes: Map<String, String> = emptyMap()
    ) {
        context.authStateDataStore.edit { preferences ->
            preferences[authStatePreferencesKey] = authState.name
            preferences[authStateAttributesPreferencesKey] = Json.encodeToString(attributes)
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
            setAuthState(AuthState.LoggedOut)
        } else {
            userLoggedIn()
        }
    }

    private suspend fun loginWithPhone(phoneNumber: String) {
        try {
            val options = AWSCognitoAuthSignInOptions.builder()
                .authFlowType(AuthFlowType.CUSTOM_AUTH_WITHOUT_SRP)
                .build()
            Amplify.Auth.signIn()
            userLoggedIn()
        } catch (error: AmplifyException) {
            handleError(error)
        } catch (error: Exception) {
            // TODO
        }
    }

    private suspend fun register(user: User) {
        val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(user.phoneNumber, "IL")
        val signUpOptions = AWSCognitoAuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.phoneNumberVerified(), formattedPhoneNumber)
            .userAttribute(AuthUserAttributeKey.phoneNumber(), formattedPhoneNumber)
            .userAttribute(AuthUserAttributeKey.email(), user.email)
            .userAttribute(AuthUserAttributeKey.birthdate(), Date(user.dateOfBirth).toString())
            .userAttribute(AuthUserAttributeKey.name(), user.firstName)
            .userAttribute(AuthUserAttributeKey.familyName(), user.lastName)
            .build()


        try {
            Amplify.Auth.signUp(
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
            setAuthState(
                AuthState.ConfirmationRequired(),
                mapOf("email" to user.email, "phoneNumber" to user.phoneNumber)
            )
        } catch (error: AmplifyException) {
            if (error is UsernameExistsException) {
                Amplify.Auth.resendSignUpCode(user.email)
            } else {
                handleError(error)
            }
        } catch (error: Exception) {
            // TODO
        }
    }

    private suspend fun loginWithGoogle(activity: Activity) {
        try {
            Amplify.Auth.signInWithSocialWebUI(
                AuthProvider.google(),
                activity
            )
            userLoggedIn()
        } catch (error: AmplifyException) {
            handleError(error)
            throw error
        } catch (error: Exception) {
            throw error
        }
    }

    private suspend fun logout() {
        Amplify.Auth.signOut()
        usersDataSource.clearUser()
        setAuthState(AuthState.LoggedOut)
    }

    private suspend fun handleError(error: AmplifyException) {
        when (error) {
            is SignedInException -> userLoggedIn()
            else -> Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }

    private suspend fun userLoggedIn() {
        try {
            val userId = Amplify.Auth.getCurrentUser().userId
            val email = Amplify.Auth.fetchUserAttributes()
                .firstOrNull { it.key == AuthUserAttributeKey.email() }?.value
            setUser()
            if (isUserRegistered(userId)) {
                setAuthState(AuthState.LoggedIn)
            } else {
                setAuthState(AuthState.RegistrationRequired(), mapOf("email" to email.toString()))
            }
        } catch (error: AmplifyException) {
            handleError(error)
        } catch (error: Exception) {
            setAuthState(AuthState.LoggedOut)
        }
    }

    private suspend fun confirmCode(code: String) {
        try {
            Amplify.Auth.confirmSignUp(
                Amplify.Auth.getCurrentUser().username,
                code
            )
            setUser()
            setAuthState(AuthState.LoggedIn)
        } catch (error: AmplifyException) {
            handleError(error)
        } catch (error: Exception) {
            println()
            // TODO
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

    override suspend fun onAuth(authEvent: AuthEvent) {
        when (authEvent) {
            is AuthEvent.LoginWithGoogle -> loginWithGoogle(authEvent.activity)
            is AuthEvent.LoginWithPhone -> loginWithPhone(authEvent.phoneNumber)
            is AuthEvent.ConfirmCode -> confirmCode(authEvent.code)
            is AuthEvent.Register -> register(authEvent.user)
            is AuthEvent.Logout -> logout()
        }
    }

    override suspend fun isUserLoggedIn(): Boolean =
        try {
            context.authStateDataStore.data.first()[authStatePreferencesKey] == AuthState.LoggedIn.name
        } catch (e: Exception) {
            false
        }


    override suspend fun isUserRegistered(userId: String): Boolean =
        try {
            usersClient.getUser(userId) != null
        } catch (e: Exception) {
            throw e
//            false
        }

    override suspend fun saveUser(user: User) = usersDataSource.saveUser(user)
    override suspend fun getUser(): User? = usersDataSource.getUser()
    override suspend fun getAuthState(): CommonFlow<AuthState> =
        try {
            context.authStateDataStore.data.map {
                val authStateString = it[authStatePreferencesKey]
                val attributesString = it[authStateAttributesPreferencesKey]
                if (authStateString.isNullOrEmpty()) {
                    return@map AuthState.LoggedOut
                }
                var attributes: Map<String, String> = emptyMap()
                if (!attributesString.isNullOrEmpty()) {
                    attributes = Json.decodeFromString(attributesString)
                }
                AuthState.fromString(authStateString).setAttributes(attributes)
            }.toCommonFlow()
        } catch (e: Exception) {
            flowOf(AuthState.LoggedOut).toCommonFlow()
        }


}