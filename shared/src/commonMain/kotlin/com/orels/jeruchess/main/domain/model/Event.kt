package com.orels.jeruchess.main.domain.model

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: Long,
    val price: Long,
    val currency: String,
    val roundNumber: Int,
    val eventType: String,
    val eventFormat: String,
    val isRatingIsrael: Boolean,
    val isRatingFide: Boolean,
    val ratingType: String,
)