package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.core.domain.exceptions.CouldNotFindUserException
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.data.chess_user_data.toBoolean
import com.orels.jeruchess.main.data.chess_user_data.toLong
import com.orels.jeruchess.main.data.users.toUser
import com.orels.jeruchess.main.domain.data.events.Events
import com.orels.jeruchess.main.domain.data.events.EventsDataSource
import com.orels.jeruchess.main.domain.model.Currency
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.EventParticipant
import database.EventEntity

class SqlDelightEventsDataSource(
    private val database: JeruChessDatabase
) : EventsDataSource {

    val db = database.jeruchessQueries
    override suspend fun getAllEvents(): List<Event> =
        db
            .getEvents()
            .executeAsList()
            .map {
                Event(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    date = it.date,
                    price = it.price,
                    currency = Currency.valueOf(it.currency),
                    roundNumber = it.roundNumber.toInt(),
                    eventType = it.eventType,
                    eventFormat = it.eventFormat,
                    isRatingIsrael = it.isRatingIsrael.toBoolean(),
                    isRatingFide = it.isRatingFide.toBoolean(),
                    ratingType = it.ratingType,
                    gameId = it.gameId
                )
            }



    override suspend fun registerToEvent(eventParticipant: EventParticipant) =
        with(eventParticipant) {
            db.insertEventParticipant(
                eventId = eventId,
                userId = userId,
                isPaid = isPaid.toLong(),
                paidAmount = paidAmount,
                paymentType = paymentType?.name,
                paidAt = paidAt,
                isActive = true.toLong()
            )
        }


    override suspend fun addEvents(events: Events) {
        events.forEach {
            with(it) {
                db.insertEvent(
                    id = id,
                    name = name,
                    description = description,
                    date = date,
                    price = price,
                    currency = currency.name,
                    roundNumber = roundNumber.toLong(),
                    eventType = eventType,
                    eventFormat = eventFormat,
                    isRatingIsrael = isRatingIsrael.toLong(),
                    isRatingFide = isRatingFide.toLong(),
                    ratingType = ratingType,
                    gameId = gameId
                )
            }
        }
    }

    override suspend fun clear() = db.clearAllEvents()


}