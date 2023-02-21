package com.orels.jeruchess.main.domain.data.chess_user_data

interface ChessUserDataClient {
    suspend fun getChessUserData()
    fun insertChessUserData()
    fun updateChessUserData()
    fun deleteChessUserData()
}