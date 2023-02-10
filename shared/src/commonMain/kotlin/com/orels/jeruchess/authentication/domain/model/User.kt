package com.orels.jeruchess.authentication.domain.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val isGuest: Boolean = true
)