package com.orels.jeruchess.main.data.games

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.games.GamesClient
import com.orels.jeruchess.main.domain.model.Games
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class KtorGamesClient(val httpClient: HttpClient) : GamesClient {
    private val baseUrl = "games"
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun getGamesByEventIds(eventIds: List<String>): Games {
        val result = httpClient.get {
            url("${NetworkConstants.BASE_URL}/$baseUrl")
        }
        val games = json.decodeFromString<List<GameDto>>(result.body())
        return games.map { it.toGame() }
    }
}