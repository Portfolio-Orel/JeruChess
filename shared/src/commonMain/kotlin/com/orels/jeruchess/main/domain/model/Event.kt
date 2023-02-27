package com.orels.jeruchess.main.domain.model

enum class Currency {
    ILS,
    USD,
}

data class Event(
    var id: String,
    var name: String,
    var description: String,
    var date: Long,
    var price: Long,
    var currency: Currency = Currency.ILS,
    var roundNumber: Int,
    var eventType: String,
    var eventFormat: String,
    var isRatingIsrael: Boolean,
    var isRatingFide: Boolean,
    var ratingType: String,
    var gameId: String
)