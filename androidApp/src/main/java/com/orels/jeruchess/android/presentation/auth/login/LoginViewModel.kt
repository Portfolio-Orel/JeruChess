package com.orels.jeruchess.android.presentation.auth.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.android.domain.annotation.AuthConfigFile
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import com.orels.jeruchess.authentication.presentation.AuthEvent
import com.orels.jeruchess.authentication.presentation.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: AuthClient,
    private val dataSource: AuthDataSource,
    private val authInteractor: AuthInteractor,
    @AuthConfigFile private val config: ConfigFile
) : ViewModel() {

    private val viewModel by lazy {
        AuthViewModel(client, coroutineScope = viewModelScope, dataSource = dataSource)
    }

    val state = viewModel.state

    fun loginWithGoogle(activity: Activity) {
        viewModelScope.launch {
//            authInteractor.loginWithGoogle(activity)
            authInteractor.loginWithPhone("+972543056286")
        }
    }

    fun onEvent(event: AuthEvent, activity: Activity) {
        if (event is AuthEvent.Login)
            viewModelScope.launch {
                authInteractor.login("event.email", event.password)
            } else {
            viewModel.onEvent(event)
        }
    }
}