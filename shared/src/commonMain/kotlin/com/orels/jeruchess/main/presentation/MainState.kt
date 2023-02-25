package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.main.domain.model.ClubData
import com.orels.jeruchess.main.domain.model.Event

data class MainState(
    val clubData: ClubData? = null,
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)