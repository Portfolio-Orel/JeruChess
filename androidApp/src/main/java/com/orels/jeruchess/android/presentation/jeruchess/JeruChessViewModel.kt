package com.orels.jeruchess.android.presentation.jeruchess

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.main.domain.data.main.MainClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JeruChessViewModel @Inject constructor(
    private val client: MainClient,
    private val authInteractor: AuthInteractor
) : ViewModel() {
    var state by mutableStateOf(JeruChessState())

    init {
        observeAuthState()
        viewModelScope.launch {
            client.getClub().also {
                state = state.copy(
                    clubData = it,
                    isLoading = false
                )
            }
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authInteractor.getAuthState()
                .catch {
                    state = state.copy(
                        isLoading = false
                    )
                }
                .collect {
                    state = state.copy(
                        authState = it,
                        isLoading = false
                    )
                }
        }
    }

    fun navigateToClub(context: Context) {
        val uri =
            Uri.parse("geo:0,0?q=${state.clubData?.address}")
        val intentUri = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intentUri)
    }

}