package com.orels.jeruchess.android.presentation.jeruchess

import com.orels.jeruchess.main.domain.model.ClubData

data class JeruChessState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val clubData: ClubData? = null,
)