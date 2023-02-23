package com.orels.jeruchess.main.data.events

import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.domain.data.events.EventsDataSource

class SqlDelightEventsDataSource(
    db: JeruChessDatabase
) : EventsDataSource {
}