package com.orels.jeruchess.main.domain.data.games

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.Games

interface GamesDataSource {
    suspend fun getAllGames(): Games
    suspend fun getGamesFlow(): CommonFlow<Games>
    suspend fun insertGames(games: Games)
    suspend fun clear()
}