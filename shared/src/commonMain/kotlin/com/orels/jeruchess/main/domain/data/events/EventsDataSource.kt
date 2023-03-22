package com.orels.jeruchess.main.domain.data.events

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.Events

interface EventsDataSource {
    suspend fun getAllEvents(): Events
    suspend fun insertEventParticipants(eventParticipants: List<EventParticipant>)
    suspend fun insertEvents(events: Events)
    suspend fun getEventsFlow(): CommonFlow<Events>
    suspend fun clear()
}