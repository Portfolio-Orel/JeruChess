package com.orels.jeruchess.android.presentation.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.android.domain.AuthEvent
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.main.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {
    var state by mutableStateOf(RegisterState())

    fun onEvent(event: RegisterEvent) =
        when (event) {
            is RegisterEvent.SetPhoneNumber -> state = state.copy(phoneNumber = event.phoneNumber)
            is RegisterEvent.SetPlayerNumber -> state =
                state.copy(playerNumber = event.playerNumber)
            is RegisterEvent.SetFirstName -> state = state.copy(firstName = event.firstName)
            is RegisterEvent.SetLastName -> state = state.copy(lastName = event.lastName)
            is RegisterEvent.SetEmail -> state = state.copy(email = event.email)
            is RegisterEvent.SetGender -> state = state.copy(gender = event.gender)
            is RegisterEvent.SetDateOfBirth -> state = state.copy(dateOfBirth = event.dateOfBirth)
            is RegisterEvent.Register -> register()
        }


    private fun register() {
        viewModelScope.launch {
            authInteractor.onAuth(
                AuthEvent.Register(
                    user = User(
                        phoneNumber = state.phoneNumber,
                        playerNumber = state.playerNumber,
                        firstName = state.firstName,
                        lastName = state.lastName,
                        gender = state.gender,
                        email = state.email,
                        dateOfBirth = state.dateOfBirth
                    )
                )
            )
        }
    }
}