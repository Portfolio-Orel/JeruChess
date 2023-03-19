package com.orels.jeruchess.core.data.interactor

import com.orels.jeruchess.core.domain.exceptions.CouldNotFindUserException
import com.orels.jeruchess.core.domain.interactors.EventsInteractor
import com.orels.jeruchess.core.domain.interactors.UserInteractor
import com.orels.jeruchess.main.domain.data.events.EventsClient
import com.orels.jeruchess.main.domain.data.events.EventsDataSource
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsClient
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.PaymentType
import com.orels.jeruchess.main.domain.model.User

class EventsInteractorImpl constructor(
    private val eventsDataSource: EventsDataSource,
    private val eventsClient: EventsClient,
    private val eventsParticipantsClient: EventsParticipantsClient,
    private val userInteractor: UserInteractor
): EventsInteractor {
    override suspend fun getAllEvents(): List<Event> {
//        eventsDataSource
        return emptyList()
    }

    override suspend fun registerToEvent(event: Event,
                                         isPaid: Boolean,
                                         paidAt: Long?,
                                         paidAmount: Long?,
                                         paymentType: PaymentType?) =
        userInteractor.getCachedUser()?.let {
            eventsParticipantsClient.addEventParticipants(
                listOf(
                    EventParticipant(
                        userId = it.id,
                        eventId = event.id,
                        isPaid = isPaid,
                        paidAt = paidAt,
                        paidAmount = paidAmount,
                        paymentType = paymentType,
                        isActive = true
                    )
                )
            )
        } ?: throw CouldNotFindUserException()
    }