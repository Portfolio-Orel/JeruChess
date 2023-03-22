package com.orels.jeruchess.main.data.games

import com.orels.jeruchess.main.domain.model.Game
import com.orels.jeruchess.main.domain.model.GameType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GameDto(
    @SerialName("id") val id: String,
    @SerialName("time_start_min") val timeStartMin: Long,
    @SerialName("increment_before_time_control_sec") val incrementBeforeTimeControlSec: Long,
    @SerialName("moves_num_to_time_control") val movesNumToTimeControl: Long?,
    @SerialName("time_bump_after_time_control_min") val timeBumpAfterTimeControlMin: Long?,
    @SerialName("increment_after_time_control_sec") val incrementAfterTimeControlSec: Long?,
    @SerialName("type") val type: String
) {
    fun toGame() = Game(
        id = id,
        timeStartMin = timeStartMin,
        incrementBeforeTimeControlSec = incrementBeforeTimeControlSec,
        movesNumToTimeControl = movesNumToTimeControl,
        timeBumpAfterTimeControlMin = timeBumpAfterTimeControlMin,
        incrementAfterTimeControlSec = incrementAfterTimeControlSec,
        type = GameType.valueOf(type.uppercase())
    )
}