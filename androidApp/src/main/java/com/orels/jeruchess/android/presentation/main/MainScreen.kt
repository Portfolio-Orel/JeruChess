package com.orels.jeruchess.android.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orels.jeruchess.android.domain.extensions.getEventParticipants
import com.orels.jeruchess.android.domain.extensions.maxRounds
import com.orels.jeruchess.android.presentation.main.components.EventCard
import com.orels.jeruchess.android.presentation.main.components.EventDetails
import com.orels.jeruchess.main.presentation.MainEvent
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

    if (state.selectedEvent != null) {
        EventDetails(
            event = state.selectedEvent!!,
            onDismiss = { viewModel.onEvent(MainEvent.ClearSelectedEvent) },
            isRegistered = viewModel.isRegistered(state.selectedEvent!!.id),
            onUnregister = { viewModel.onEvent(MainEvent.UnregisterFromEvent(state.selectedEvent!!)) },
            onPayByCardClick = { viewModel.onEvent(MainEvent.PayByCard(state.selectedEvent!!)) },
            onLaterClick = { viewModel.onEvent(MainEvent.PayLater(state.selectedEvent!!)) },
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(state.events.size) { index ->
            val event = state.events[index]
            val game = state.games.firstOrNull { it.id == event.gameId }
            val maxRounds = state.events.maxRounds(event.name)
            EventCard(
                event = event,
                onRegister = { viewModel.onEvent(MainEvent.RegisterToEvent(it)) },
                onCancelRegistration = { viewModel.onEvent(MainEvent.UnregisterFromEvent(it)) },
                game = game,
                modifier = Modifier.padding(8.dp),
                maxRounds = if (maxRounds == 0) 1 else maxRounds,
                participants = state.eventsParticipants.getEventParticipants(event.id),
                isRegistered = viewModel.isRegistered(event.id),
                isLoading = state.loadingEventName == event.name,
            )
        }
    }
}