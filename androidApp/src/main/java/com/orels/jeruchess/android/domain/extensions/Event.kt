package com.orels.jeruchess.android.domain.extensions

import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.Events
import com.orels.jeruchess.main.domain.model.EventsParticipants

fun Events.maxRounds(name: String): Int =
    count { it.name == name }

fun EventsParticipants.getEventParticipants(eventId: String): List<EventParticipant> =
    filter { it.eventId == eventId }