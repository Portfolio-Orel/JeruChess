package com.orels.jeruchess.android.presentation.auth.register

import com.orels.jeruchess.main.domain.model.Gender
import com.orels.jeruchess.main.domain.model.User

data class RegisterState(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val gender: Gender = Gender.None,
    val phoneNumber: String = "",
    val playerNumber: String = "", // TODO
    val dateOfBirth: Long = 0L,
    val code: String = "",

    val user: User = User(),

    val isLoading: Boolean = false,

    )