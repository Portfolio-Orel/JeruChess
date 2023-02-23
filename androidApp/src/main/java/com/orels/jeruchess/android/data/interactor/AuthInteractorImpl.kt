package com.orels.jeruchess.android.data.interactor

import android.app.Activity
import android.content.Context
import android.telephony.PhoneNumberUtils
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.options.AuthFlowType
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.kotlin.core.Amplify
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.core.util.CommonFlow
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

    val auth = Firebase.auth

    override suspend fun loginWithGoogle(activity: Activity) {

    }

    override suspend fun loginWithPhone(phoneNumber: String, activity: Activity) {
        val formattedPhoneNumber = PhoneNumberUtils.formatNumberToE164(phoneNumber, "IL")
        val signInOptions: AuthSignInOptions = AWSCognitoAuthSignInOptions.builder()
            .authFlowType(AuthFlowType.CUSTOM_AUTH_WITH_SRP)
            .metadata(mapOf("phoneNumber" to formattedPhoneNumber))
            .build()
        try {
            Amplify.Auth.signIn(
                formattedPhoneNumber,
                null,
                signInOptions,
            )
            println("success")
        } catch (e: Exception) {
            println(e)
        }
//        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)

//            == ConnectionResult.SUCCESS
//        ) {
//            println() // AIzaSyBMfhHxb58xjUi2acv-dzXyLecpuAir898
//        } else {
//            println()
//        }
//        FirebaseAuth.getInstance().setLanguageCode("en")
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(formattedPhoneNumber)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(activity)
//            .requireSmsValidation(true)
//            .setCallbacks(phoneAuthCallback)
//            .build()
//        auth.setLanguageCode("en")
//        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun register(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun isUserRegistered(email: String?, phoneNumber: String?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun getUserFlow(): CommonFlow<User> {
        TODO("Not yet implemented")
    }

    private val phoneAuthCallback =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                println("onVerificationCompleted")
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                println("onVerificationFailed")
            }

        }
}
