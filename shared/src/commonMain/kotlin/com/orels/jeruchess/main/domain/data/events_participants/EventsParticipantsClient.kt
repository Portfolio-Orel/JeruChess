package com.orels.jeruchess.main.domain.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant

interface EventsParticipantsClient {
    suspend fun getAllEventsParticipants(eventId: String): List<EventParticipant>
    suspend fun addEventParticipants(eventParticipant: List<EventParticipant>)
    suspend fun removeEventParticipants(eventParticipant: List<EventParticipant>)

}