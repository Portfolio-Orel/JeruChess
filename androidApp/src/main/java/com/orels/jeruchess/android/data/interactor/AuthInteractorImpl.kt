package com.orels.jeruchess.android.data.interactor

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.exceptions.invalidstate.SignedInException
import com.amplifyframework.auth.cognito.exceptions.service.UserNotConfirmedException
import com.amplifyframework.auth.cognito.exceptions.service.UsernameExistsException
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.options.AuthFlowType
import com.amplifyframework.auth.options.AuthSignUpOptions
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
        logout()
        if (!isUserLoggedIn()) {
            usersDataSource.clearUser()
            setAuthState(AuthState.LoggedOut)
        } else {
            userLoggedIn()
        }
    }

    private suspend fun confirmSignIn(code: String) {
        try {
            Amplify.Auth.confirmSignIn(code)
            userLoggedIn()
        } catch (error: AmplifyException) {
            handleError(error)
        } catch (error: Exception) {
            throw error
        }
    }

    private suspend fun loginWithPhone(phoneNumber: String) {
        try {
            val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(phoneNumber, "IL")
            val authSignInOptions = AWSCognitoAuthSignInOptions.builder()
                .authFlowType(AuthFlowType.CUSTOM_AUTH_WITHOUT_SRP)
                .build()
            Amplify.Auth.signIn(formattedPhoneNumber, options = authSignInOptions)
        } catch (error: AmplifyException) {
            handleError(error, phoneNumber)
        } catch (error: Exception) {
            println()
        }
    }

    private suspend fun register(user: User) {
        val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(user.phoneNumber, "IL")
        val signUpOptions = AuthSignUpOptions.builder()
            .userAttributes(
                mutableListOf(
                    AuthUserAttribute(AuthUserAttributeKey.email(), user.email),
                    AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), formattedPhoneNumber)
                )
            ).build()
        try {
            val result = Amplify.Auth.signUp(
                formattedPhoneNumber,
                PasswordGenerator.generateStrongPassword(),
                signUpOptions
            )
            if (result.userId == null) throw Exception("userId is null after sign up")
            usersClient.createUser(user, result.userId!!)
            setAuthState(
                AuthState.RegistrationRequired(),
                mapOf("phoneNumber" to formattedPhoneNumber)
            )
        } catch (error: AmplifyException) {
            if (error is UsernameExistsException) {
                Amplify.Auth.resendSignUpCode(formattedPhoneNumber)
            } else {
                handleError(error)
            }
        } catch (error: Exception) {
            println()
        }
    }

    private suspend fun logout() {
        Amplify.Auth.signOut()
        usersDataSource.clearUser()
        setAuthState(AuthState.LoggedOut)
    }

    private suspend fun handleError(error: AmplifyException, phoneNumber: String = "") {
        try {
            when (error) {
                is SignedInException -> userLoggedIn()
                is UserNotConfirmedException -> confirmationRequired(phoneNumber)
                else -> Log.e("MyAmplifyApp", "Unhandled error", error)
            }
        } catch (error: Exception) {
            Log.e("MyAmplifyApp", "Error in handling error", error)
        }
    }

    private suspend fun confirmationRequired(phoneNumber: String) {

        val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(phoneNumber, "IL")
        Amplify.Auth.resendSignUpCode(formattedPhoneNumber)
        setAuthState(AuthState.ConfirmationRequired(), mapOf("phoneNumber" to formattedPhoneNumber))
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

    private suspend fun confirmSignUp(user: User, code: String) {
        try {
            val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(user.phoneNumber, "IL")
            Amplify.Auth.confirmSignUp(formattedPhoneNumber, code)
            val userId = Amplify.Auth.getCurrentUser().userId
            val token =
                (Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession).userPoolTokensResult.value?.accessToken
            if (token == null) {
                logout()
                return
            }
            user.id = userId
            user.token = token
            saveUser(user = user)
            setAuthState(
                AuthState.ConfirmationRequired(),
                mapOf("email" to user.email, "phoneNumber" to user.phoneNumber)
            )
            setUser()
            setAuthState(AuthState.LoggedIn)
        } catch (error: AmplifyException) {
            handleError(error)
        } catch (error: Exception) {
            println()
        }
    }

    private suspend fun completeRegistration(user: User) {
        try {
            usersClient.completeRegistration(user)
            usersDataSource.updateUser(user)
            saveUser(user = user)
            setAuthState(AuthState.LoggedIn)
        } catch (error: AmplifyException) {
            handleError(error)
        } catch (error: Exception) {
            println()
        }
    }

    private suspend fun updateUser(user: User) {
        try {
            usersClient.updateUser(user)
            usersDataSource.updateUser(user)
            saveUser(user = user)
            setAuthState(AuthState.LoggedIn)
        } catch (error: AmplifyException) {
            handleError(error)
        } catch (error: Exception) {
            println()
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

    private suspend fun saveUser(user: User) = usersDataSource.saveUser(user)

    override suspend fun onAuth(authEvent: AuthEvent) {
        when (authEvent) {
            is AuthEvent.LoginWithPhone -> loginWithPhone(authEvent.phoneNumber)
            is AuthEvent.ConfirmSignUp -> confirmSignUp(
                user = authEvent.user,
                code = authEvent.code
            )
            is AuthEvent.ConfirmSignIn -> confirmSignIn(authEvent.code)
            is AuthEvent.Register -> register(authEvent.user)
            is AuthEvent.Logout -> logout()
            is AuthEvent.UpdateUser -> updateUser(authEvent.user)
            is AuthEvent.CompleteRegistration -> completeRegistration(authEvent.user)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean =
        try {
            context.authStateDataStore.data.first()[authStatePreferencesKey] == AuthState.LoggedIn.name
        } catch (e: Exception) {
            false
        }

    override suspend fun isUserRegistered(userId: String): Boolean =
        usersClient.isUserRegistered(userId)

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