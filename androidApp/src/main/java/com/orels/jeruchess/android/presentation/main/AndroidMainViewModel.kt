package com.orels.jeruchess.android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.core.domain.interactors.EventsInteractor
import com.orels.jeruchess.main.domain.data.games.GamesDataSource
import com.orels.jeruchess.main.domain.data.main.MainClient
import com.orels.jeruchess.main.domain.data.main.MainDataSource
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.orels.jeruchess.main.presentation.MainEvent
import com.orels.jeruchess.main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidMainViewModel @Inject constructor(
    private val client: MainClient,
    private val usersDataSource: UsersDataSource,
    private val gamesDataSource: GamesDataSource,
    private val eventsInteractor: EventsInteractor,
    private val dataSource: MainDataSource,
) : ViewModel() {

    private val viewModel by lazy {
        MainViewModel(
            client = client,
            usersDataSource = usersDataSource,
            eventsInteractor = eventsInteractor,
            gamesDataSource = gamesDataSource,
            dataSource = dataSource,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    init {
        viewModel.onEvent(MainEvent.initClubsData)
    }

    fun onEvent(event: MainEvent) {
        viewModel.onEvent(event)
    }

    fun isRegistered(eventId: String): Boolean = state
        .value
        .eventsParticipants
        .any { it.eventId == eventId && it.userId == state.value.user?.id }
}