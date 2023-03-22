package com.orels.jeruchess.main.data.payment

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.payment.PaymentInteractor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PaymentInteractorImpl(val httpClient: HttpClient) : PaymentInteractor {

    private val baseUrl = "payment"
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun getPaymentUrl(eventId: String): String? =
        try {
            val result = httpClient.get {
                url("${NetworkConstants.BASE_URL}/$baseUrl/$eventId/payment")
            }
            json.decodeFromString<PaymentDto>(result.body()).paymentUrl
        } catch (e: Exception) {
            null
        }

}