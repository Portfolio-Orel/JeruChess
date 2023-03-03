package com.orels.jeruchess.android.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orels.jeruchess.android.domain.extensions.getEventParticipants
import com.orels.jeruchess.android.domain.extensions.maxRounds
import com.orels.jeruchess.android.presentation.main.components.EventCard
import com.orels.jeruchess.android.presentation.main.components.EventDetails
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.presentation.MainState

@Composable
fun MainScreen(
    navController: NavController,
    state: MainState,
    viewModel: AndroidMainViewModel
) {
    MainContent(viewModel = viewModel)
}

@Composable
fun MainContent(
    viewModel: AndroidMainViewModel,
) {
    val state = viewModel.state.value
    val selectedEvent: MutableState<Event?> = remember { mutableStateOf(null) }
    if (selectedEvent.value != null) {
        EventDetails(
            event = selectedEvent.value!!,
            onDismiss = { selectedEvent.value = null },
            isRegistered = viewModel.isRegistered(selectedEvent.value!!.id),
            onUnregister = { selectedEvent.value = null },
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(state.events.size) { index ->
            val event = state.events[index]
            val game = state.games.firstOrNull { it.id == event.gameId }
            val maxRounds = state.events.maxRounds(event.id)
            EventCard(
                event = event,
                onRegister = { selectedEvent.value = event },
                onCancelRegistration = { selectedEvent.value = event },
                game = game,
                modifier = Modifier.padding(8.dp),
                maxRounds = if (maxRounds == 0) 1 else maxRounds,
                participants = state.eventsParticipants.getEventParticipants(event.id),
                isRegistered = viewModel.isRegistered(event.id),
            )
        }
    }
}