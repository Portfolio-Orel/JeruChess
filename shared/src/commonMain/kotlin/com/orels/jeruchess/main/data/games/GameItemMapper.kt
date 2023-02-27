package com.orels.jeruchess.main.data.games

import com.orels.jeruchess.main.domain.model.Game
import com.orels.jeruchess.main.domain.model.GameType
import database.GameEntity

fun GameEntity.toGame() = Game(
    id = id,
    timeStart = timeStartMin,
    incrementBeforeTimeControl = incrementBeforeTimeControl,
    movesNumToTimeControl = movesNumToTimeControl,
    timeBumpAfterTimeControl = timeBumpAfterTimeControl,
    incrementAfterTimeControl = incrementAfterTimeControl,
    type = GameType.valueOf(type)
)