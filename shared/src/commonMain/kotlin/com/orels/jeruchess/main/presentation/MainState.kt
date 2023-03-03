package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.main.domain.model.*

data class MainState(
    val clubData: ClubData? = null,
    val events: List<Event> = emptyList(),
    val eventsParticipants: List<EventParticipant> = emptyList(),
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)