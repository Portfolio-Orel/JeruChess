package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.data.chess_user_data.toBoolean
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsDataSource
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.PaymentType

class SqlDelightEventsParticipantsDataSource(
    database: JeruChessDatabase
) : EventsParticipantsDataSource {

    val db = database.jeruchessQueries
    override suspend fun getAllEventsParticipants(eventId: String): List<EventParticipant> {
        return db.getEventParticipants(eventId).executeAsList().map {
            EventParticipant(
                userId = it.userId,
                eventId = it.eventId,
                isPaid = it.isPaid.toBoolean(),
                paidAt = it.paidAt,
                paidAmount = it.paidAmount,
                paymentType = PaymentType.valueOf(it.paymentType ?: PaymentType.Card.name),
                isActive = it.isActive.toBoolean()
            )
        }
    }

    override suspend fun addEventParticipants(eventParticipant: List<EventParticipant>) {
        TODO("Not yet implemented")
    }

    override suspend fun removeEventParticipants(eventParticipant: List<EventParticipant>) {
        TODO("Not yet implemented")
    }
}