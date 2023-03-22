package com.orels.jeruchess.main.domain.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.EventsParticipants

interface EventsParticipantsClient {
    suspend fun getAllEventsParticipants(eventIds: List<String>): EventsParticipants
    suspend fun addEventParticipant(eventParticipant: EventParticipant): EventsParticipants
    suspend fun removeEventParticipants(eventParticipant: EventsParticipants): EventsParticipants
}