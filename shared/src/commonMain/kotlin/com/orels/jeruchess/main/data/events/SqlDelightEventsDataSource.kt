package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.data.chess_user_data.toBoolean
import com.orels.jeruchess.main.data.chess_user_data.toLong
import com.orels.jeruchess.main.domain.data.events.EventsDataSource
import com.orels.jeruchess.main.domain.exception.NullEventParticipantIdException
import com.orels.jeruchess.main.domain.model.Currency
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.Events
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map

class SqlDelightEventsDataSource(
    database: JeruChessDatabase
) : EventsDataSource {

    val db = database.jeruchessQueries
    override suspend fun getAllEvents(): Events =
        db
            .getEvents()
            .executeAsList()
            .map {
                Event(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    date = it.date,
                    price = it.price.toFloatOrNull() ?: throw Exception("price is not a float"),
                    currency = Currency.valueOf(it.currency),
                    roundNumber = it.roundNumber.toInt(),
                    eventType = it.eventType,
                    eventFormat = it.eventFormat,
                    isRatingIsrael = it.isRatingIsrael.toBoolean(),
                    isRatingFide = it.isRatingFide.toBoolean(),
                    gameId = it.gameId
                )
            }


    override suspend fun insertEventParticipants(eventParticipants: List<EventParticipant>) {
        db.transaction {
            eventParticipants.forEach {
                with(it) {
                    db.insertEventParticipant(
                        id = id ?: throw NullEventParticipantIdException(
                            userId = userId,
                            eventId = eventId
                        ),
                        userId = userId,
                        eventId = eventId,
                        isPaid = isPaid.toLong(),
                        paidAt = paidAt,
                        paidAmount = paidAmount,
                        paymentType = paymentType?.name,
                        isActive = isActive.toLong()
                    )
                }
            }
        }
    }


    override suspend fun insertEvents(events: Events) {
        events.forEach {
            with(it) {
                db.insertEvent(
                    id = id,
                    name = name,
                    description = description,
                    date = date,
                    price = price.toString(),
                    currency = currency.name,
                    roundNumber = roundNumber.toLong(),
                    eventType = eventType,
                    eventFormat = eventFormat,
                    isRatingIsrael = isRatingIsrael.toLong(),
                    isRatingFide = isRatingFide.toLong(),
                    gameId = gameId
                )
            }
        }
    }

    override suspend fun getEventsFlow(): CommonFlow<Events> = db.getEvents()
        .asFlow()
        .mapToList()
        .map { it.map { eventEntity -> eventEntity.toEvent() } }.toCommonFlow()

    override suspend fun clear() = db.clearAllEvents()


}