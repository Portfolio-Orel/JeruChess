package com.orels.jeruchess.main.domain.data.events_participants

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.EventsParticipants

interface EventsParticipantsDataSource {
    suspend fun getAllEventsParticipants(): EventsParticipants
    suspend fun getEventParticipant(userId: String, eventId: String): EventParticipant
    suspend fun removeEventParticipants(eventParticipant: EventsParticipants)
    suspend fun insertEventsParticipants(eventsParticipants: EventsParticipants)
    suspend fun getEventsParticipantsFlow(): CommonFlow<EventsParticipants>

    suspend fun clear()
}