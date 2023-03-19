package com.orels.jeruchess.core.domain.interactors

import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.PaymentType

interface EventsInteractor {
    suspend fun getAllEvents(): List<Event>
    suspend fun registerToEvent(
                                event: Event,
                                isPaid: Boolean = false,
                                paidAt: Long? = null,
                                paidAmount: Long? = null,
                                paymentType: PaymentType? = null
    )
}