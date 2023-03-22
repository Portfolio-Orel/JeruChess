package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.events.EventsClient
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.Events
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

data class PaymentFormResponse(
    @SerialName("payment_url") val paymentUrl: String,
    @SerialName("error_code") val errorCode: String
)

class KtorEventsClient(
    private val httpClient: HttpClient
) : EventsClient {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val baseUrl = "events"
    override suspend fun getEvent(eventId: String): Event {
        val result = httpClient.get {
            url("${NetworkConstants.BASE_URL}/$baseUrl/$eventId")
        }
        return result.body<EventDto>().toEvent()
    }

    override suspend fun getAllEvents(): Events {
        val result = httpClient.get {
            url("${NetworkConstants.BASE_URL}/$baseUrl")
        }
        val events = json.decodeFromString<List<EventDto>>(result.body())
        return events.map { it.toEvent() }
    }

    override suspend fun insertEvent(event: Event) {
        try {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/$baseUrl")
                setBody(event.toEventDto())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateEvent(event: Event) {
        try {
            httpClient.put {
                url("${NetworkConstants.BASE_URL}/$baseUrl")
                setBody(event.toEventDto())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteEvent(event: Event) {
        try {
            httpClient.delete {
                url("${NetworkConstants.BASE_URL}/$baseUrl/${event.id}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPaymentUrl(event: Event): String {
        try {
            val result = httpClient.get {
                url("${NetworkConstants.BASE_URL}/${event.id}/payments/form")
            }.body<PaymentFormResponse>()
            return result.paymentUrl
        } catch (e: Exception) {
            throw e
        }
    }
}