package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.main.domain.model.Event
import database.EventEntity

fun EventEntity.toEvent() = Event(
    id = id,
    name = name,
    description = description,
    date = date,
    price = price,
    currency = currency,
    minAge = minAge,
    minRating = minRating,
    maxRating = maxRating
)