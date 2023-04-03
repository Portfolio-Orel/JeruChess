package com.orels.jeruchess.main.domain.model

enum class Currency {
    ILS,
    USD,
}

typealias Events = List<Event>

data class Event(
    var id: String,
    var name: String,
    var description: String,
    var date: Long,
    var price: Float,
    var currency: Currency = Currency.ILS,
    var roundNumber: Int,
    var gameFormatId: String,
    var isRatingIsrael: Boolean,
    var isRatingFide: Boolean,
    var gameId: String
)