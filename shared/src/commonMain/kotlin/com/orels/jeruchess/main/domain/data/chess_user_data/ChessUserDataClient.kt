package com.orels.jeruchess.main.domain.data.chess_user_data

import com.orels.jeruchess.main.domain.model.ChessUserData

interface ChessUserDataClient {
    suspend fun getChessUserData(userId: String): ChessUserData?
    suspend fun insertChessUserData(chessUserData: ChessUserData)
    suspend fun updateChessUserData(chessUserData: ChessUserData)
    suspend fun deleteChessUserData(chessUserData: ChessUserData)
}