package com.orels.jeruchess.main.data.games

import com.orels.jeruchess.main.domain.model.Game
import com.orels.jeruchess.main.domain.model.GameType
import database.GameEntity

fun GameEntity.toGame() = Game(
    id = id,
    timeStartMin = timeStartMin,
    incrementBeforeTimeControlSec = incrementBeforeTimeControlSec,
    movesNumToTimeControl = movesNumToTimeControl,
    timeBumpAfterTimeControlMin = timeBumpAfterTimeControlMin,
    incrementAfterTimeControlSec = incrementAfterTimeControlSec,
    type = GameType.valueOf(type)
)