package com.orels.jeruchess.core.domain.interactors

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.Events
import com.orels.jeruchess.main.domain.model.EventsParticipants
import com.orels.jeruchess.main.domain.model.PaymentType

interface EventsInteractor {

    suspend fun initData()
    suspend fun getAllEvents(): Events
    suspend fun registerToEvent(
                                event: Event,
                                isPaid: Boolean = false,
                                paidAt: Long? = null,
                                paidAmount: Long? = null,
                                paymentType: PaymentType? = null
    )
    suspend fun unregisterFromEvent(event: Event)
    suspend fun getAllEventsParticipants(): EventsParticipants

    suspend fun getEventsFlow(): CommonFlow<Events>
    suspend fun getEventsParticipantsFlow(): CommonFlow<EventsParticipants>
}