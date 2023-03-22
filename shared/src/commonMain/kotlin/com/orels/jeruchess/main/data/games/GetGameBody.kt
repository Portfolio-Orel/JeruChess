package com.orels.jeruchess.main.data.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetGameBody(
    @SerialName("event_ids") val eventIds: List<String>
)