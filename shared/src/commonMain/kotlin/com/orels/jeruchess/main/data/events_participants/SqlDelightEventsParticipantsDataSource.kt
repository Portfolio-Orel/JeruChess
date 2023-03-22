package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.data.chess_user_data.toBoolean
import com.orels.jeruchess.main.data.chess_user_data.toLong
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsDataSource
import com.orels.jeruchess.main.domain.exception.NullEventParticipantIdException
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.EventsParticipants
import com.orels.jeruchess.main.domain.model.PaymentType
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map

class SqlDelightEventsParticipantsDataSource(
    database: JeruChessDatabase
) : EventsParticipantsDataSource {

    val db = database.jeruchessQueries
    override suspend fun getAllEventsParticipants(): EventsParticipants {
        return db.getAllEventsParticipants().executeAsList().map {
            EventParticipant(
                id = it.id,
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

    override suspend fun getEventParticipant(userId: String, eventId: String): EventParticipant =
        db.getEventParticipant(userId = userId, eventId = eventId)
            .executeAsOne()
            .let {
                EventParticipant(
                    id = it.id,
                    userId = it.userId,
                    eventId = it.eventId,
                    isPaid = it.isPaid.toBoolean(),
                    paidAt = it.paidAt,
                    paidAmount = it.paidAmount,
                    paymentType = PaymentType.valueOf(it.paymentType ?: PaymentType.Card.name),
                    isActive = it.isActive.toBoolean()
                )
            }

    override suspend fun removeEventParticipants(eventParticipant: EventsParticipants) {
        db.transaction {
            eventParticipant.forEach {
                db.deleteEventParticipant(
                    userId = it.userId,
                    eventId = it.eventId
                )
            }
        }
    }

    override suspend fun insertEventsParticipants(eventsParticipants: EventsParticipants) {
        db.transaction {
            eventsParticipants.forEach {
                db.insertEventParticipant(
                    id = it.id ?: throw NullEventParticipantIdException(
                        userId = it.userId,
                        eventId = it.eventId
                    ),
                    userId = it.userId,
                    eventId = it.eventId,
                    isPaid = it.isPaid.toLong(),
                    paidAt = it.paidAt,
                    paidAmount = it.paidAmount,
                    paymentType = it.paymentType?.name,
                    isActive = it.isActive.toLong()
                )
            }
        }
    }

    override suspend fun getEventsParticipantsFlow(): CommonFlow<EventsParticipants> =
        db.getAllEventsParticipants()
            .asFlow()
            .mapToList()
            .map { it.map { eventParticipantEntity -> eventParticipantEntity.toEventParticipant() } }
            .toCommonFlow()

    override suspend fun clear() {
        db.clearAllEventsParticipants()
    }

}