package com.orels.jeruchess.android.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import com.orels.jeruchess.authentication.presentation.AuthEvent
import com.orels.jeruchess.authentication.presentation.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: AuthClient,
    private val dataSource: AuthDataSource,
) : ViewModel() {

    private val viewModel by lazy {
        AuthViewModel(client, coroutineScope = viewModelScope, dataSource = dataSource)
    }

    val state = viewModel.state

    fun onEvent(event: AuthEvent) {
        viewModel.onEvent(event)
    }
}