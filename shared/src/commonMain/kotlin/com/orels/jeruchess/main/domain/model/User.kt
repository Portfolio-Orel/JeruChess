package com.orels.jeruchess.main.domain.model

data class User(
    val id: String,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val gender: String = "MALE",
    val phoneNumber: String = "",
    val playerNumber: String = "",
    val dateOfBirth: Long = 0L,
)