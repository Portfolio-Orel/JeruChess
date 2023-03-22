package com.orels.jeruchess.main.domain.data.events

import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.Events

interface EventsClient {
    suspend fun getEvent(eventId: String): Event?
    suspend fun getAllEvents(): Events
    suspend fun insertEvent(event: Event)
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(event: Event)

    suspend fun getPaymentUrl(event: Event): String
}