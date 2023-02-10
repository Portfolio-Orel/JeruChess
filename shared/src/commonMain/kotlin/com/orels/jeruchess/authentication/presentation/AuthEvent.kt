package com.orels.jeruchess.authentication.presentation

sealed class AuthEvent {
    data class Login(val username: String, val password: String) : AuthEvent()
    object Register : AuthEvent()
    object Logout : AuthEvent()
}