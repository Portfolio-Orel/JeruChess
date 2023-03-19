package com.orels.jeruchess.main.domain.data.events

import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.EventParticipant

typealias Events = List<Event>

interface EventsDataSource {
    suspend fun getAllEvents(): List<Event>
    suspend fun registerToEvent(eventParticipant: EventParticipant)
    suspend fun addEvents(events: Events)
    suspend fun clear()
}