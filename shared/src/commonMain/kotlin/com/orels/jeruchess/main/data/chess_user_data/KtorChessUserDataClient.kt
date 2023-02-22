package com.orels.jeruchess.main.data.chess_user_data

import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.main.domain.data.chess_user_data.ChessUserDataClient
import com.orels.jeruchess.main.domain.model.ChessUserData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class KtorChessUserDataClient(
    private val httpClient: HttpClient
) : ChessUserDataClient {

    private val baseUrl = NetworkConstants.BASE_URL + "chess_user_data"
    override suspend fun getChessUserData(userId: String): ChessUserData {
        try {
            val result = httpClient.get {
                url("$baseUrl/$userId")
            }
            return result.body<ChessUserDataDto>().toChessUserData()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun insertChessUserData(chessUserData: ChessUserData) {
        try {
            httpClient.post {
                url(baseUrl)
                setBody(chessUserData.toChessUserDataDto())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateChessUserData(chessUserData: ChessUserData) {
        try {
            httpClient.put {
                url(baseUrl)
                setBody(chessUserData.toChessUserDataDto())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteChessUserData(chessUserData: ChessUserData) {
       try {
            httpClient.delete {
                url("$baseUrl/${chessUserData.userId}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}