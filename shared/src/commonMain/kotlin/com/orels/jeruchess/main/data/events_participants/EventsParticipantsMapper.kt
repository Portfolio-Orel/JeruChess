package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant
import database.EventParticipantEntity

fun EventParticipantEntity.toEventParticipant() = EventParticipant(
    userId = userId,
    eventId = eventId,
)
