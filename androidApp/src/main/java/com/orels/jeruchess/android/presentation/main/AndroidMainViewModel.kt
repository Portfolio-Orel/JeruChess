package com.orels.jeruchess.android.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orels.jeruchess.main.domain.data.events.EventsClient
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
    private val dataSource: MainDataSource,
) : ViewModel() {

    private val viewModel by lazy {
        MainViewModel(client, eventClient, dataSource, coroutineScope = viewModelScope)
    }

    init {

    }

    val state = viewModel.state

    init {
        onEvent(MainEvent.GetClub)
    }

    fun onEvent(event: MainEvent) {
        viewModel.onEvent(event)
    }

    fun openGoogleMaps(context: Context) {
        val gmmIntentUri =
            Uri.parse("geo:0,0?q=${state.value.clubData?.address}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}