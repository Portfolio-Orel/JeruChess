package com.orels.jeruchess.main.domain.data.games

import com.orels.jeruchess.main.domain.model.Games

interface GamesClient {
    suspend fun getGamesByEventIds(eventIds: List<String>): Games
}