package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsClient
import com.orels.jeruchess.main.domain.model.EventParticipant
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class KtorEventsParticipantsClient(
    private val httpClient: HttpClient,
) : EventsParticipantsClient {

    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val baseUrl =
        "${NetworkConstants.BASE_URL}/participants"

    override suspend fun getAllEventsParticipants(eventIds: List<String>): List<EventParticipant> {
        try {
            val result = httpClient.get {
                url("$baseUrl/event_ids?ids=${eventIds.joinToString(",")}")
            }
            return json.decodeFromString<List<EventParticipantDto>>(result.body())
                .map { it.toEventParticipant() }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun addEventParticipant(eventParticipant: EventParticipant): List<EventParticipant> {
        try {
            val eventId = eventParticipant.eventId
            val result = httpClient.post {
                url("$baseUrl/${eventId}")
                setBody(eventParticipant.toEventParticipantDto())
                contentType(ContentType.Application.Json)
            }
            println()
            val participants =
                json.decodeFromString<List<EventParticipantDto>>(result.body())
                    .map { it.toEventParticipant() }
            return participants
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun removeEventParticipants(eventParticipant: List<EventParticipant>) {
        try {
            val eventId = eventParticipant.firstOrNull()?.eventId ?: return
            val participantsIds = eventParticipant.map { it.userId }
            val result = httpClient.delete {
                url("$baseUrl/$eventId/participant_ids?ids=${participantsIds.joinToString(",")}")
                contentType(ContentType.Application.Json)
                setBody(eventParticipant.toEventParticipantsDto())
            }
            println()
            val eventParticipant =
                json.decodeFromString<List<EventParticipantDto>>(result.body())
                    .map { it.toEventParticipant() }
        } catch (e: Exception) {
            throw e
        }
    }
}