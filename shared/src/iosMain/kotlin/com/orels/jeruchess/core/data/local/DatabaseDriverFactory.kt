package com.orels.jeruchess.core.data.local

import com.orels.jeruchess.database.JeruChessDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(JeruChessDatabase.Schema, "jeruchess.db")
    }
}