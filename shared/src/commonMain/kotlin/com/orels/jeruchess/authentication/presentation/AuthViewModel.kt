package com.orels.jeruchess.authentication.presentation

import com.orels.jeruchess.authentication.domain.client.AuthClient
import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import com.orels.jeruchess.core.util.CommonStateFlow
import com.orels.jeruchess.core.util.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val client: AuthClient,
    private val dataSource: AuthDataSource,
    coroutineScope: CoroutineScope?,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(AuthState())
    val state: CommonStateFlow<AuthState> = _state
        .toCommonStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            dataSource.getUser().collectLatest { user ->
                _state.update {
                    it.copy(
                        isAuthorized = user != null
                    )
                }
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> login(event.username, event.password)
            is AuthEvent.Logout -> logout()
            else -> {}
        }
    }

    private fun login(username: String, password: String) {
        _state.update {
            it.copy(
                isLoadingLogin = true
            )
        }
        viewModelScope.launch {
            try {
                val user = client.login(username, password)
                user?.let { dataSource.addUser(it) }
                _state.update {
                    it.copy(
                        isLoadingLogin = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingLogin = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun logout() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            try {
                client.logout()
                dataSource.deleteUser()
                _state.update {
                    it.copy(
                        isAuthorized = false,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isAuthorized = false,
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
}