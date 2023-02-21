package com.orels.jeruchess.main.data.chess_user_data

import com.orels.jeruchess.main.domain.model.ChessUserData
import database.ChessUserDataEntity

fun ChessUserDataEntity.toChessUserData() = ChessUserData(
    userId = userId,
    rating = rating,
)