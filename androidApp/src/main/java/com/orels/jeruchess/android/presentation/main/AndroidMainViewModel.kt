package com.orels.jeruchess.android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.main.domain.data.MainClient
import com.orels.jeruchess.main.domain.data.MainDataSource
import com.orels.jeruchess.main.presentation.MainEvent
import com.orels.jeruchess.main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidMainViewModel @Inject constructor(
    private val client: MainClient,
    private val dataSource: MainDataSource,
) : ViewModel() {

    private val viewModel by lazy {
        MainViewModel(client, dataSource, coroutineScope = viewModelScope)
    }

    val state = viewModel.state

    init {
        onEvent(MainEvent.GetClub)
    }

    fun onEvent(event: MainEvent) {
        viewModel.onEvent(event)
    }
}