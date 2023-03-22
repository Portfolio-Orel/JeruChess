package com.orels.jeruchess.main.domain.model

enum class GameType {
    CLASSICAL,
    RAPID,
    BLITZ,
    BULLET
}

typealias Games = List<Game>

/**
 * @param id
 * @param timeStartMin - time in minutes
 * @param incrementBeforeTimeControlSec - time in seconds
 * @param movesNumToTimeControl - number of moves to time control
 * @param timeBumpAfterTimeControlMin - time in minutes
 * @param incrementAfterTimeControlSec - time in seconds
 * @param type - game type (classic, rapid, blitz, bullet)
 */
data class Game(
    var id: String,
    var timeStartMin: Long,
    var incrementBeforeTimeControlSec: Long,
    var movesNumToTimeControl: Long?,
    var timeBumpAfterTimeControlMin: Long?,
    var incrementAfterTimeControlSec: Long?,
    var type: GameType
)