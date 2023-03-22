package com.orels.jeruchess.core.data.interactor

import com.orels.jeruchess.core.domain.exceptions.CouldNotFindUserException
import com.orels.jeruchess.core.domain.interactors.EventsInteractor
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.data.events.EventsClient
import com.orels.jeruchess.main.domain.data.events.EventsDataSource
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsClient
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsDataSource
import com.orels.jeruchess.main.domain.data.games.GamesClient
import com.orels.jeruchess.main.domain.data.games.GamesDataSource
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.orels.jeruchess.main.domain.model.*

class EventsInteractorImpl constructor(
    private val eventsClient: EventsClient,
    private val eventsDataSource: EventsDataSource,
    private val eventsParticipantsClient: EventsParticipantsClient,
    private val eventsParticipantsDataSource: EventsParticipantsDataSource,
    private val gamesClient: GamesClient,
    private val gamesDataSource: GamesDataSource,
    private val usersDataSource: UsersDataSource,
) : EventsInteractor {
    override suspend fun initData(
    ) {
        try {
            val events = eventsClient.getAllEvents()
            val games = gamesClient.getGamesByEventIds(events.map { it.id })
            val eventsParticipants =
                eventsParticipantsClient.getAllEventsParticipants(events.map { it.id })

            gamesDataSource.clear()
            gamesDataSource.insertGames(games)
            eventsDataSource.clear()
            eventsDataSource.insertEvents(events)
            eventsParticipantsDataSource.clear()
            eventsParticipantsDataSource.insertEventsParticipants(eventsParticipants)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getAllEvents(): List<Event> = eventsDataSource.getAllEvents()

    override suspend fun registerToEvent(
        event: Event,
        isPaid: Boolean,
        paidAt: Long?,
        paidAmount: Long?,
        paymentType: PaymentType?
    ) {
        usersDataSource.getUser()?.let {
            val eventParticipant = EventParticipant(
                userId = it.id,
                eventId = event.id,
                isPaid = isPaid,
                paidAt = paidAt,
                paidAmount = paidAmount,
                paymentType = paymentType,
                isActive = true
            )
            val eventParticipants = eventsParticipantsClient.addEventParticipant(eventParticipant)

            eventsParticipantsDataSource.insertEventsParticipants(eventParticipants)
        } ?: throw CouldNotFindUserException()
    }

    override suspend fun unregisterFromEvent(event: Event) {
        usersDataSource.getUser()?.let {
            val eventParticipant = eventsParticipantsDataSource.getEventParticipant(
                userId = it.id,
                eventId = event.id
            )
            val eventsParticipants =
                eventsParticipantsClient.removeEventParticipants(listOf(eventParticipant))
            eventsParticipantsDataSource.removeEventParticipants(eventsParticipants)
        } ?: throw CouldNotFindUserException()
    }

    override suspend fun getAllEventsParticipants(): EventsParticipants =
        eventsParticipantsDataSource.getAllEventsParticipants()

    override suspend fun getEventsFlow(): CommonFlow<Events> =
        eventsDataSource.getEventsFlow()

    override suspend fun getEventsParticipantsFlow(): CommonFlow<EventsParticipants> =
        eventsParticipantsDataSource.getEventsParticipantsFlow()
}