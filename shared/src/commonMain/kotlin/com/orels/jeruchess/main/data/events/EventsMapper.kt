package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.main.domain.model.Currency
import com.orels.jeruchess.main.domain.model.Event
import database.EventEntity

fun EventEntity.toEvent() = Event(
    id = id,
    name = name,
    description = description,
    date = date,
    price = price.toFloatOrNull() ?: throw Exception("price is not a float"),
    currency = Currency.valueOf(currency.uppercase()),
    roundNumber = roundNumber.toInt(),
    gameFormatId = gameFormatId,
    isRatingIsrael = isRatingIsrael == 1L,
    isRatingFide = isRatingFide == 1L,
    gameId = gameId
)