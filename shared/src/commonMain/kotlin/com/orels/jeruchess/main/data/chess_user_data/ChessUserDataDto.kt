package com.orels.jeruchess.main.data.chess_user_data

import com.orels.jeruchess.main.domain.model.ChessUserData
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ChessUserDataDto(
    @SerialName("user_id") val userId: String,
    @SerialName("rating") val rating: Long,
    @SerialName("is_profile_active") val isProfileActive: Boolean,
) {
    fun toChessUserData() = ChessUserData(
        userId = userId,
        rating = rating,
        isProfileActive = isProfileActive,
    )
}

fun ChessUserData.toChessUserDataDto() = ChessUserDataDto(
    userId = userId,
    rating = rating,
    isProfileActive = isProfileActive,
)