package com.orels.jeruchess.main.data.games

import com.orels.jeruchess.main.domain.data.games.GamesClient
import com.orels.jeruchess.main.domain.model.Game
import com.orels.jeruchess.utils.StubData
import io.ktor.client.*

class KtorGamesClient(val httpClient: HttpClient) : GamesClient {
    override suspend fun getGamesByEventIds(eventIds: List<String>): List<Game> = StubData.Games

}