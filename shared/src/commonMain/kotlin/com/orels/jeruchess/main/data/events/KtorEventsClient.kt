package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.events.EventsClient
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.utils.StubData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName

data class PaymentFormResponse(
    @SerialName("payment_url") val paymentUrl: String,
    @SerialName("error_code") val errorCode: String
)

class KtorEventsClient(
    private val httpClient: HttpClient
) : EventsClient {

    private val baseUrl = "events"
    override suspend fun getEvent(eventId: String): Event {
        try {
            val result = httpClient.get {
                url("${NetworkConstants.BASE_URL}/$baseUrl/$eventId")
            }
            return result.body<EventDto>().toEvent()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllEvents(): List<Event> {
        try {
//            val result = httpClient.get {
//                url(baseUrl)
//            }
//            val events = result.body<List<EventDto>>()
//            return events.map { it.toEvent() }
            return StubData.Events
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun insertEvent(event: Event) {
        try {
            httpClient.post {
                url(baseUrl)
                setBody(event.toEventDto())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateEvent(event: Event) {
        try {
            httpClient.put {
                url(baseUrl)
                setBody(event.toEventDto())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteEvent(event: Event) {
        try {
            httpClient.delete {
                url("$baseUrl/${event.id}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPaymentUrl(event: Event): String {
        try {
            val result = httpClient.get {
                url("$baseUrl/${event.id}/payments/form")
            }.body<PaymentFormResponse>()
            return result.paymentUrl
        } catch (e: Exception) {
            throw e
        }
    }
}