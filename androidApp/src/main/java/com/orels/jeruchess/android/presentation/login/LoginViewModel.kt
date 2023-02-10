package com.orels.jeruchess.android.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.presentation.AuthEvent
import com.orels.jeruchess.authentication.presentation.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: AuthClient
) : ViewModel() {
 private val viewModel by lazy {
     AuthViewModel(client, coroutineScope = viewModelScope)
 }

    val state = viewModel.state

    fun onEvent(event: AuthEvent) {
        viewModel.onEvent(event)
    }
}