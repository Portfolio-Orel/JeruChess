package com.orels.jeruchess.android.presentation.jeruchess

import com.orels.jeruchess.android.domain.AuthState
import com.orels.jeruchess.main.domain.model.ClubData

data class JeruChessState(
    val isLoading: Boolean = true,
    val authState: AuthState = AuthState.LoggedOut,
    val clubData: ClubData? = null,
)