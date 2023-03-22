package com.orels.jeruchess.main.data.chess_user_data

import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.domain.data.chess_user_data.ChessUserDataDataSource
import com.orels.jeruchess.main.domain.model.ChessUserData

class SqlDelightChessUserDataDataSource(
    db: JeruChessDatabase
) : ChessUserDataDataSource {
    private val chessUserDataQueries = db.jeruchessQueries

    override suspend fun getChessUserData(userId: String): ChessUserData? = chessUserDataQueries
        .getChessUserData()
        .executeAsOneOrNull()
        ?.toChessUserData()

    override fun insertChessUserData(chessUserData: ChessUserData) = chessUserDataQueries
        .insertChessUserData(
            userId = chessUserData.userId,
            rating = chessUserData.rating,
            isProfileActive = chessUserData.isProfileActive.toLong(),
        )

    override fun updateChessUserData(chessUserData: ChessUserData) = chessUserDataQueries
        .updateChessUserData(
            userId = chessUserData.userId,
            rating = chessUserData.rating,
            isProfileActive = chessUserData.isProfileActive.toLong(),
        )

    override fun deleteChessUserData(chessUserData: ChessUserData) = chessUserDataQueries
        .deleteChessUserData(
            userId = chessUserData.userId,
        )

}

fun Boolean?.toLong() = if (this != null && this) 1L else 0L