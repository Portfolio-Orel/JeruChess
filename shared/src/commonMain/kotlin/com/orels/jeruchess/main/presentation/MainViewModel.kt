package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.core.domain.interactors.EventsInteractor
import com.orels.jeruchess.core.util.CommonStateFlow
import com.orels.jeruchess.core.util.toCommonStateFlow
import com.orels.jeruchess.main.domain.data.games.GamesDataSource
import com.orels.jeruchess.main.domain.data.main.MainClient
import com.orels.jeruchess.main.domain.data.main.MainDataSource
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val client: MainClient,
    private val usersDataSource: UsersDataSource,
    private val gamesDataSource: GamesDataSource,
    private val eventsInteractor: EventsInteractor,
    private val dataSource: MainDataSource,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(MainState())
    val state: CommonStateFlow<MainState> = _state.toCommonStateFlow()

    init {
        viewModelScope.launch {
            try {
                eventsInteractor.initData()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message
                    )
                }
            }
            observeUser()
            observeGames()
            observeEvents()
            observeEventsParticipants()
        }
    }

        private fun observeEventsParticipants() {
            viewModelScope.launch {
                eventsInteractor.getEventsParticipantsFlow().collect { eventsParticipants ->
                    _state.update {
                        it.copy(
                            eventsParticipants = eventsParticipants
                        )
                    }
                }
            }
        }

        private fun observeEvents() {
            viewModelScope.launch {
                eventsInteractor.getEventsFlow().collect { events ->
                    _state.update {
                        it.copy(
                            events = events
                        )
                    }
                }
            }
        }

        private fun observeGames() {
            viewModelScope.launch {
                gamesDataSource.getGamesFlow().collect { games ->
                    _state.update {
                        it.copy(
                            games = games
                        )
                    }
                }
            }
        }

        private fun observeUser() {
            viewModelScope.launch {
                usersDataSource.getUserFlow().collect { user ->
                    _state.update {
                        it.copy(
                            user = user
                        )
                    }
                }
            }
        }

        private fun initClubsData() {
            viewModelScope.launch {
                try {
                    val clubData = client.getClub()
                    _state.update {
                        it.copy(
                            clubData = clubData,
                            isLoading = false,
                            error = null
                        )
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
            }
        }

        fun onEvent(event: MainEvent) {
            when (event) {
                is MainEvent.initClubsData -> {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    initClubsData()
                }
                is MainEvent.AddClub -> {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    viewModelScope.launch {
                        try {
                            client.addClub(event.clubData)
                            dataSource.addClub(event.clubData)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = null
                                )
                            }
                        } catch (e: Exception) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                        }
                    }
                }
                is MainEvent.UpdateClub -> {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    viewModelScope.launch {
                        try {
                            client.updateClub(event.clubData)
                            dataSource.updateClub(event.clubData)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = null
                                )
                            }
                        } catch (e: Exception) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                        }
                    }
                }
                is MainEvent.DeleteClub -> {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    viewModelScope.launch {
                        try {
                            client.deleteClub(event.clubData)
                            dataSource.deleteClub(event.clubData)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = null
                                )
                            }
                        } catch (e: Exception) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                        }
                    }
                }
                MainEvent.NavigateToClubAddress -> {}
                is MainEvent.PayByCard -> TODO()
                is MainEvent.PayByCash -> TODO()
                MainEvent.ClearSelectedEvent -> {
                    _state.update {
                        it.copy(
                            selectedEvent = null
                        )
                    }
                }
                is MainEvent.RegisterToEvent -> {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    viewModelScope.launch {
                        try {
                            eventsInteractor.registerToEvent(
                                event.event
                            )
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    selectedEvent = event.event,
                                    error = null
                                )
                            }
                        } catch (e: Exception) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                        }
                    }

                }
                is MainEvent.SetSelectedEvent -> {
                    _state.update {
                        it.copy(
                            selectedEvent = event.event
                        )
                    }
                }
                is MainEvent.UnregisterFromEvent -> {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    viewModelScope.launch {
                        try {
                            eventsInteractor.unregisterFromEvent(event.event)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    selectedEvent = event.event,
                                    error = null
                                )
                            }

                        } catch (e: Exception) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                        }
                    }

                }
            }
        }
    }