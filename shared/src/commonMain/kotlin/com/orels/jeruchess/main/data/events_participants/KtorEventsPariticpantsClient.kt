package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsClient
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.utils.StubData
import io.ktor.client.*
import io.ktor.client.request.*

class KtorEventsParticipantsClient(
    private val httpClient: HttpClient,
) : EventsParticipantsClient {
    private fun baseUrl(eventId: String) =
        NetworkConstants.BASE_URL + "/events/$eventId/participants"

    override suspend fun getAllEventsParticipants(eventId: String): List<EventParticipant> {
        try {
//            val result = httpClient.get {
//                url(baseUrl(eventId))
//            }
//            return result.body<List<EventParticipantDto>>().toEventParticipants()
            return StubData.EventsParticipants.filter { it.eventId == eventId }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun addEventParticipants(eventParticipant: List<EventParticipant>) {
        try {
            val eventId = eventParticipant.firstOrNull()?.eventId ?: return
            httpClient.post {
                url(baseUrl(eventId))
                setBody(eventParticipant.toEventParticipantsDto())
                header("Content-Type", "application/json")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun removeEventParticipants(eventParticipant: List<EventParticipant>) {
        try {
            val eventId = eventParticipant.firstOrNull()?.eventId ?: return
            httpClient.delete {
                url(baseUrl(eventId))
                setBody(eventParticipant.toEventParticipantsDto())
            }
        } catch (e: Exception) {
            throw e
        }
    }
}