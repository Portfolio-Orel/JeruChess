package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.main.domain.model.Currency
import com.orels.jeruchess.main.domain.model.Event
import database.EventEntity

fun EventEntity.toEvent() = Event(
    id = id,
    name = name,
    description = description,
    date = date,
    price = price,
    currency = Currency.valueOf(currency),
    roundNumber = roundNumber.toInt(),
    eventType = eventType,
    eventFormat = eventFormat,
    isRatingIsrael = isRatingIsrael == 1L,
    isRatingFide = isRatingFide == 1L,
    ratingType = ratingType,
    gameId = gameId
)