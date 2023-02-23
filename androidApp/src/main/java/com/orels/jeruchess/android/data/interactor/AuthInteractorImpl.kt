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
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.options.AuthConfirmSignInOptions
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.main.domain.data.users.UsersClient
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.orels.jeruchess.main.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
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
            val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(phoneNumber, "IL")
            val options: AuthSignInOptions = AWSCognitoAuthSignInOptions.builder()
                .metadata(
                    mapOf(
                        AuthUserAttributeKey.phoneNumberVerified().toString() to formattedPhoneNumber
                    )
                )
                .metadata(
                    mapOf(
                        AuthUserAttributeKey.phoneNumber().toString() to formattedPhoneNumber
                    )
                )
                .build()
//
//            Amplify.Auth.signIn(null, null, options, { result ->
//                Log.i("AuthQuickstart", "")
//            }, { error ->
//                Log.e("AuthQuickstart", error.toString())
//            })


            Amplify.Auth.signIn(formattedPhoneNumber, null, options,
                {
                    Log.i("AuthDemo", "Confirmation code resent")
                },
                { e ->
                    Log.e("AuthDemo", "Confirmation code resend failed", e)
                }
            )
//            Amplify.Auth.signUp(formattedPhoneNumber, PasswordGenerator.generateStrongPassword(), options,
//                { result ->
//                    if (result.nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP) {
//                        Log.i("AuthDemo", "Sign up complete")
////                        result.userId
//                    }
//                    Log.i("AuthDemo", "Sign up successful, confirmation code sent")
//                },
//                { error ->
//                    Log.e("AuthDemo", "Sign up failed", error)
//                    Amplify.Auth.resendSignUpCode(phoneNumber,
//                        {
//                            Log.i("AuthDemo", "Confirmation code resent")
//                        },
//                        { e ->
//                            Log.e("AuthDemo", "Confirmation code resend failed", e)
//                        }
//                    )
//                }
//            )
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    override suspend fun register(email: String, password: String) {

        val confirmOptions = AuthConfirmSignInOptions.defaults()

        Amplify.Auth.confirmSignIn(email, confirmOptions,
            {
                Log.i("AuthDemo", "Sign in succeeded")
            },
            { e ->
                Log.e("AuthDemo", "Sign in failed", e)
            }
        )
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
