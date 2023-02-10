package com.orels.jeruchess.authentication.presentation

import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    val client: AuthClient,
    private val dataSource: AuthDataSource,
    private val coroutineScope: CoroutineScope?,
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(AuthState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AuthState())

    private val authJob: Job? = null

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
                viewModelScope.launch {
                    try {
                        val user = client.login(event.username, event.password)
                        user?.let { dataSource.addUser(it) }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null
                            )
                        }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = e.message
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}