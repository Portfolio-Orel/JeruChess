package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.PaymentType
import database.EventParticipantEntity

fun EventParticipantEntity.toEventParticipant() = EventParticipant(
    userId = userId,
    eventId = eventId,
    isPaid = isPaid == 1L,
    paidAmount = paidAmount,
    paidAt = paidAt,
    paymentType = paymentType?.let { PaymentType.valueOf(it) } ?: PaymentType.Card,
    isActive = isActive == 1L
)
