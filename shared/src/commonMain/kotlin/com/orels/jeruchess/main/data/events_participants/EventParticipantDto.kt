package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant
import kotlinx.serialization.SerialName

data class EventParticipantDto(
    @SerialName("user_id") val userId: String,
    @SerialName("event_id") val eventId: String,
) {
    fun toEventParticipant() = EventParticipant(
        userId = userId,
        eventId = eventId,
    )
}

fun List<EventParticipantDto>.toEventParticipants() = map { it.toEventParticipant() }

fun EventParticipant.toEventParticipantDto() = EventParticipantDto(
    userId = userId,
    eventId = eventId,
)

fun List<EventParticipant>.toEventParticipantsDto() = map { it.toEventParticipantDto() }