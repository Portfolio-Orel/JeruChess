package com.orels.jeruchess.android.domain.extensions

import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.EventsParticipants
import com.orels.jeruchess.main.domain.model.Events

fun Events.maxRounds(eventId: String): Int =
    count { it.id == eventId }

fun EventsParticipants.getEventParticipants(eventId: String): List<EventParticipant> =
    filter { it.eventId == eventId }