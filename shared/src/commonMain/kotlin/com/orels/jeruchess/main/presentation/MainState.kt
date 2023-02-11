package com.orels.jeruchess.main.presentation

import com.orels.jeruchess.main.domain.model.ClubData

data class MainState(
    val clubData: ClubData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)