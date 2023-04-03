package com.orels.jeruchess.core.data.local

import android.content.Context
import com.orels.jeruchess.database.JeruChessDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory (
    private val context: Context
){
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(JeruChessDatabase.Schema, context, "chess.db")
    }
}