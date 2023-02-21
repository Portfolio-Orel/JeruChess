package com.orels.jeruchess.main.domain.model

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: Long,
    val price: Long,
    val currency: String,
    val minAge: Long,
    val minRating: Long,
    val maxRating: Long
)