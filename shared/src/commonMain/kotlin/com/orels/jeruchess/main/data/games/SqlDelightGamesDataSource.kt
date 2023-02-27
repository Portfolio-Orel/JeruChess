package com.orels.jeruchess.main.data.games

import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.domain.data.games.GamesDataSource

class SqlDelightGamesDataSource(
    private val db: JeruChessDatabase
) : GamesDataSource {
}