package com.orels.jeruchess.main.domain.model

data class ChessUserData(
    val userId: String,
    val rating: Long,
    val isProfileActive: Boolean,
)