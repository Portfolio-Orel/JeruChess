package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.core.util.CommonStateFlow
import com.orels.jeruchess.core.util.toCommonStateFlow
import com.orels.jeruchess.main.domain.data.MainClient
import com.orels.jeruchess.main.domain.data.MainDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val client: MainClient,
    private val dataSource: MainDataSource,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(MainState())
    val state: CommonStateFlow<MainState> = _state.toCommonStateFlow()

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