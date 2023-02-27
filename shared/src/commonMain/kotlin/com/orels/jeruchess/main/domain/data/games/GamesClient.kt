package com.orels.jeruchess.main.domain.data.games

import com.orels.jeruchess.main.domain.model.Game

interface GamesClient {
    suspend fun getGamesByEventIds(eventIds: List<String>): List<Game>
}