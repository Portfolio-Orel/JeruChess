package com.orels.jeruchess.main.domain.data.payment

interface PaymentInteractor {
    suspend fun getPaymentUrl(eventId: String): String?
}