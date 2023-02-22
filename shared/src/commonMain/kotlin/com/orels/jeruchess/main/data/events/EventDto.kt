package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.main.domain.model.Event
import kotlinx.serialization.SerialName

data class EventDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: Long,
    @SerialName("price") val price: Long,
    @SerialName("currency") val currency: String,
    @SerialName("min_age") val minAge: Long,
    @SerialName("min_rating") val minRating: Long,
    @SerialName("max_rating") val maxRating: Long,
) {
    fun toEvent() = Event(
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
}

fun Event.toEventDto() = EventDto(
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