package com.orels.jeruchess.android

import android.app.Application
import com.google.android.gms.safetynet.SafetyNet
import com.google.firebase.FirebaseApp
import com.orels.jeruchess.android.domain.annotation.AuthConfigFile
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    @AuthConfigFile
    lateinit var config: ConfigFile

    @Inject
    lateinit var authInteractor: AuthInteractor
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
//        authInteractor.initialize(config)
        val nonce = UUID.randomUUID().toString().toByteArray()
        SafetyNet.getClient(this)
            .attest(nonce, "AIzaSyBMfhHxb58xjUi2acv-dzXyLecpuAir898")

    }
}