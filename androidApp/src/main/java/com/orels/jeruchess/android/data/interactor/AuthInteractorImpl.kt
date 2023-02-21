package com.orels.jeruchess.android.data.interactor

import android.app.Activity
import android.content.Context
import android.telephony.PhoneNumberUtils
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.exceptions.invalidstate.SignedInException
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.main.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
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
            val options = AuthSignUpOptions.builder()
                .userAttribute(
                    AuthUserAttributeKey.phoneNumber(),
                    PhoneNumberUtils.formatNumberToE164(phoneNumber, "IL")
                )
                .userAttribute(AuthUserAttributeKey.email(), "orelsmail@gmail.com")
                .build()

            Amplify.Auth.signUp(phoneNumber, "38qj2!cmiCkK", options,
                { result ->
                    if(result.nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP) {
                        Log.i("AuthDemo", "Sign up complete")
                    }
                    Log.i("AuthDemo", "Sign up successful, confirmation code sent")
                },
                { error ->
                    Log.e("AuthDemo", "Sign up failed", error)
                    Amplify.Auth.resendSignUpCode(phoneNumber,
                        { result ->
                            Log.i("AuthDemo", "Confirmation code resent")
                        },
                        { error ->
                            Log.e("AuthDemo", "Confirmation code resend failed", error)
                        }
                    )
                }
            )
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    override suspend fun register(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUserEmail(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }


    override suspend fun loginWithGoogle(activity: Activity) {
        try {
            Amplify.Auth.signInWithSocialWebUI(
                AuthProvider.google(),
                activity,
                { Log.i("AuthQuickstart", "Result: $it") },
                {
                    when (it) {
                        is SignedInException -> Log.i("AuthQuickstart", "Signed in already")
                        else -> Log.e("AuthQuickstart", "Sign in failed", it)
                    }
                }
            )
            Log.i(
                "MyAmplifyApp",
                "Initialized Amplify"
            )
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }
}