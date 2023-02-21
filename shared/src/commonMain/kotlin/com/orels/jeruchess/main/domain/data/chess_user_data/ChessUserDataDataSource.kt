package com.orels.jeruchess.main.domain.data.chess_user_data

import com.orels.jeruchess.main.domain.model.ChessUserData

interface ChessUserDataDataSource {
    suspend fun getChessUserData(userId: String): ChessUserData?
    fun insertChessUserData(chessUserData: ChessUserData)
    fun updateChessUserData(chessUserData: ChessUserData)
    fun deleteChessUserData(chessUserData: ChessUserData)
}