package com.orels.jeruchess.core.domain

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val isGuest: Boolean = true
)