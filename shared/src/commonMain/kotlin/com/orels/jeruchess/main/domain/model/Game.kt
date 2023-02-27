package com.orels.jeruchess.main.domain.model

enum class GameType {
    CLASSIC,
    RAPID,
    BLITZ,
    BULLET
}

/**
 * @param id
 * @param timeStart - time in minutes
 * @param incrementBeforeTimeControl - time in seconds
 * @param movesNumToTimeControl - number of moves to time control
 * @param timeBumpAfterTimeControl - time in minutes
 * @param incrementAfterTimeControl - time in seconds
 * @param type - game type (classic, rapid, blitz, bullet)
 */
data class Game(
    var id: String,
    var timeStart: Long,
    var incrementBeforeTimeControl: Long,
    var movesNumToTimeControl: Long?,
    var timeBumpAfterTimeControl: Long?,
    var incrementAfterTimeControl: Long?,
    var type: GameType
)