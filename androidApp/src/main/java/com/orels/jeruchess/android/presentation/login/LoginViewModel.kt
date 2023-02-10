package com.orels.jeruchess.android.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import com.orels.jeruchess.authentication.presentation.AuthEvent
import com.orels.jeruchess.authentication.presentation.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            dataSource.getUser().collectLatest {
                Log.v("LoginViewModel", "User: $it")
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        viewModel.onEvent(event)
    }
}