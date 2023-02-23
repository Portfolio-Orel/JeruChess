package com.orels.jeruchess.main.data.users

import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.domain.data.users.UsersDataSource

class SqlDelightUsersDataSource(
    db: JeruChessDatabase
) : UsersDataSource
{
}