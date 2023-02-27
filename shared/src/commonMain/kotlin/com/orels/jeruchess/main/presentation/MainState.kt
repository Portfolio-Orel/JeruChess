package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.main.domain.model.ClubData
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.Game

data class MainState(
    val clubData: ClubData? = null,
    val events: List<Event> = emptyList(),
    val eventsParticipants: List<EventParticipant> = emptyList(),
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)