package com.orels.jeruchess.main.domain.model

enum class Gender {
    Male,
    Female,
    None;

    companion object {
        fun fromString(value: String): Gender =
            values().firstOrNull() { it.name.lowercase() == value.lowercase() } ?: Male
    }
}

data class User(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var gender: Gender = Gender.Male,
    var phoneNumber: String = "",
    var playerNumber: String = "",
    var dateOfBirth: Long = 0L,
    var token: String? = null,
)