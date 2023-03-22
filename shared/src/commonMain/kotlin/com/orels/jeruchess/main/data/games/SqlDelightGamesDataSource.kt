package com.orels.jeruchess.main.data.games

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.domain.data.games.GamesDataSource
import com.orels.jeruchess.main.domain.model.Games
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map

class SqlDelightGamesDataSource(
    database: JeruChessDatabase
) : GamesDataSource {

    val db = database.jeruchessQueries

    override suspend fun getAllGames(): Games {
        return db.getAllGames().executeAsList().map { it.toGame() }
    }

    override suspend fun getGamesFlow(): CommonFlow<Games> = db.getAllGames()
        .asFlow()
        .mapToList()
        .map { it.map { gameEntity -> gameEntity.toGame() } }.toCommonFlow()


    override suspend fun insertGames(games: Games) {
        db.transaction {
            games.forEach {
                db.insertGame(
                    id = it.id,
                    timeStartMin = it.timeStartMin,
                    incrementBeforeTimeControlSec = it.incrementBeforeTimeControlSec,
                    movesNumToTimeControl = it.movesNumToTimeControl,
                    timeBumpAfterTimeControlMin = it.timeBumpAfterTimeControlMin,
                    incrementAfterTimeControlSec = it.incrementAfterTimeControlSec,
                    type = it.type.name
                )
            }
        }
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }
}