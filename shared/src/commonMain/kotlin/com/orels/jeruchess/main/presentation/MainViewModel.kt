package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.core.util.CommonStateFlow
import com.orels.jeruchess.core.util.toCommonStateFlow
import com.orels.jeruchess.main.domain.data.events.EventsClient
import com.orels.jeruchess.main.domain.data.main.MainClient
import com.orels.jeruchess.main.domain.data.main.MainDataSource
import com.orels.jeruchess.utils.StubEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val client: MainClient,
    private val eventsClient: EventsClient,
    private val dataSource: MainDataSource,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(MainState())
    val state: CommonStateFlow<MainState> = _state.toCommonStateFlow()

    init {
        viewModelScope.launch {
            try {
                val events = StubEvents.Events.sortedBy {
                    it.date
                }
                _state.update {
                    it.copy(
                        events = events
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message
                    )
                }
            }
        }
    }


    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.GetClub -> {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
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
        }
    }
}