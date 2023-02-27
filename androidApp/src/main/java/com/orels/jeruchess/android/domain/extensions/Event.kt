package com.orels.jeruchess.android.domain.extensions

import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.EventParticipant

fun List<Event>.maxRounds(eventId: String): Int =
    count { it.id == eventId }

fun List<EventParticipant>.getEventParticipants(eventId: String): List<EventParticipant> =
    filter { it.eventId == eventId }