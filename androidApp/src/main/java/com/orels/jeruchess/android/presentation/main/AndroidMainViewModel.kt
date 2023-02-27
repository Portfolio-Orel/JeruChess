package com.orels.jeruchess.android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.main.domain.data.events.EventsClient
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsClient
import com.orels.jeruchess.main.domain.data.games.GamesClient
import com.orels.jeruchess.main.domain.data.main.MainClient
import com.orels.jeruchess.main.domain.data.main.MainDataSource
import com.orels.jeruchess.main.presentation.MainEvent
import com.orels.jeruchess.main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidMainViewModel @Inject constructor(
    private val client: MainClient,
    private val eventClient: EventsClient,
    private val gamesClient: GamesClient,
    private val eventsParticipantsClient: EventsParticipantsClient,
    private val dataSource: MainDataSource,
) : ViewModel() {

    private val viewModel by lazy {
        MainViewModel(
            client,
            eventClient,
            gamesClient,
            eventsParticipantsClient,
            dataSource,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    init {
        viewModel.onEvent(MainEvent.GetClub)
    }
}