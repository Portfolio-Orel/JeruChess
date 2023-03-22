package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.main.domain.model.*

data class MainState(
    val clubData: ClubData? = null,
    val events: Events = emptyList(),
    val eventsParticipants: List<EventParticipant> = emptyList(),
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val loadingEventName: String? = null,
    val paymentUrl: String? = null,
    val user: User? = null,
    val selectedEvent: Event? = null,
    val error: String? = null
)