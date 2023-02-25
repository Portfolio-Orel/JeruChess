package com.orels.jeruchess.main.domain.model

data class User(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var gender: String = "MALE",
    var phoneNumber: String = "",
    var playerNumber: String = "",
    var dateOfBirth: Long = 0L,
    var token: String? = null,
)