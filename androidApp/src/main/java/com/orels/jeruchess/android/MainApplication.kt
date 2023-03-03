package com.orels.jeruchess.android

import android.app.Application
import com.google.firebase.FirebaseApp
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.android.domain.annotation.AuthConfigFile
import com.orels.jeruchess.android.domain.model.ConfigFile
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
       CoroutineScope(Dispatchers.Main).launch {
            authInteractor.initialize(config)
        }
    }
}