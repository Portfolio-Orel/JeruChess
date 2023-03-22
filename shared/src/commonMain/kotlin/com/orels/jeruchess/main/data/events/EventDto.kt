package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.main.domain.model.Currency
import com.orels.jeruchess.main.domain.model.Event
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class EventDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: String,
    @SerialName("price") val price: Float,
    @SerialName("currency") val currency: String,
    @SerialName("round_number") val roundNumber: Int,
    @SerialName("event_type") val eventType: String,
    @SerialName("event_format") val eventFormat: String,
    @SerialName("is_rating_israel") val isRatingIsrael: Boolean,
    @SerialName("is_rating_fide") val isRatingFide: Boolean,
    @SerialName("game_id") val gameId: String,
    @SerialName("is_active") val isActive: Boolean
) {
    fun toEvent() = Event(
        id = id,
        name = name,
        description = description,
        date = Instant.parse(date).toEpochMilliseconds(),
        price = price,
        currency = Currency.valueOf(currency.uppercase()),
        roundNumber = roundNumber,
        eventType = eventType,
        eventFormat = eventFormat,
        isRatingIsrael = isRatingIsrael,
        isRatingFide = isRatingFide,
        gameId = gameId
    )
}

fun Event.toEventDto() = EventDto(
    id = id,
    name = name,
    description = description,
    date = Instant.fromEpochMilliseconds(date).toString(),
    price = price,
    currency = currency.name.lowercase(),
    roundNumber = roundNumber,
    eventType = eventType,
    eventFormat = eventFormat,
    isRatingIsrael = isRatingIsrael,
    isRatingFide = isRatingFide,
    gameId = gameId,
    isActive = true
)