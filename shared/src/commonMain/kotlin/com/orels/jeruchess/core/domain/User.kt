package com.orels.jeruchess.core.domain

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val isGuest: Boolean
)