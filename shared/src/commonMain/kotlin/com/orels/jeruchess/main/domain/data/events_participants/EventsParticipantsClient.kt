package com.orels.jeruchess.main.domain.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant

interface EventsParticipantsClient {
    suspend fun getAllEventsParticipants(eventIds: List<String>): List<EventParticipant>
    suspend fun addEventParticipant(eventParticipant: EventParticipant): List<EventParticipant>
    suspend fun removeEventParticipants(eventParticipant: List<EventParticipant>)
}