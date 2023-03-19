package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.main.domain.model.ClubData
import com.orels.jeruchess.main.domain.model.Event

sealed class MainEvent {
    object GetClub : MainEvent()
    object NavigateToClubAddress : MainEvent()
    data class AddClub(val clubData: ClubData) : MainEvent()
    data class UpdateClub(val clubData: ClubData) : MainEvent()
    data class DeleteClub(val clubData: ClubData) : MainEvent()
    data class RegisterToEvent(val event: Event) : MainEvent()
    data class UnregisterFromEvent(val event: Event) : MainEvent()
    data class PayByCard(val eventId: String) : MainEvent()
    data class PayByCash(val eventId: String) : MainEvent()
    data class SetSelectedEvent(val event: Event) : MainEvent()
    object ClearSelectedEvent : MainEvent()

}