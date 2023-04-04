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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            is RegisterEvent.CompleteRegistration -> completeRegistration()
            is RegisterEvent.ConfirmCode -> confirmCode(event.code)
            is RegisterEvent.PreviousStage -> state = state.copy(stage = state.stage.previous())
        }


    private fun confirmCode(code: String) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            authInteractor.onAuth(
                AuthEvent.ConfirmSignUp(
                    user = state.user,
                    code = code,
                )
            )
            withContext(Dispatchers.Main) {
                state = state.copy(isLoading = false, stage = state.stage.next())
            }
        }
    }

    private fun completeRegistration() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            authInteractor.onAuth(
                AuthEvent.CompleteRegistration(
                    user = User(
                        firstName = state.firstName,
                        lastName = state.lastName,
                        dateOfBirth = state.dateOfBirth,
                        gender = state.gender
                    ),
                )
            )
            withContext(Dispatchers.Main) {
                state = state.copy(isLoading = false, stage = state.stage.next())
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            val user = User(
                phoneNumber = state.phoneNumber,
                playerNumber = state.playerNumber,
                firstName = state.firstName,
                lastName = state.lastName,
                gender = state.gender,
                email = state.email,
                dateOfBirth = state.dateOfBirth
            )
            state = state.copy(isLoading = true, user = user)
            authInteractor.onAuth(
                AuthEvent.Register(user = user)
            )
            withContext(Dispatchers.Main) {
                state = state.copy(isLoading = false, stage = state.stage.next())
            }
        }
    }
}