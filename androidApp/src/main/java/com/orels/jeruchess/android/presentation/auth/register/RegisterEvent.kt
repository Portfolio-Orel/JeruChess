package com.orels.jeruchess.android.presentation.auth.register

import com.orels.jeruchess.main.domain.model.Gender

sealed class RegisterEvent {
    data class SetUsername(val username: String): RegisterEvent()
    data class SetPassword(val password: String): RegisterEvent()
    data class SetConfirmPassword(val confirmPassword: String): RegisterEvent()
    data class SetPhoneNumber(val phoneNumber: String): RegisterEvent()
    data class SetPlayerNumber(val playerNumber: String): RegisterEvent()
    data class SetFirstName(val firstName: String): RegisterEvent()
    data class SetLastName(val lastName: String): RegisterEvent()
    data class SetEmail(val email: String): RegisterEvent()
    data class SetGender(val gender: Gender): RegisterEvent()
    data class SetDateOfBirth(val dateOfBirth: Long): RegisterEvent()
    data class ConfirmCode(val code: String): RegisterEvent()
    object PreviousStage: RegisterEvent()
    object CompleteRegistration: RegisterEvent()
    object Register: RegisterEvent() }