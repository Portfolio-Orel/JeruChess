package com.orels.jeruchess.main.data.users

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.orels.jeruchess.main.domain.model.User
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.map

class SqlDelightUsersDataSource(
    db: JeruChessDatabase
) : UsersDataSource {
    private val usersQueries = db.jeruchessQueries
    override suspend fun saveUser(user: User) {
        if(usersQueries.getUser().executeAsOneOrNull() != null) {
            usersQueries.deleteUser()
        }
        usersQueries.insertUser(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            gender = user.gender.name,
            email = user.email,
            phoneNumber = user.phoneNumber,
            playerNumber = user.playerNumber,
            dateOfBirth = user.dateOfBirth,
            token = user.token
        )
    }

    override suspend fun getUser(): User? = usersQueries
        .getUser()
        .executeAsOneOrNull()
        ?.toUser()

    override suspend fun getUserFlow(): CommonFlow<User?> =
        usersQueries
            .getUser()
            .asFlow()
            .map { it.executeAsOneOrNull() }
            .map { it?.toUser() }
            .toCommonFlow()

    override suspend fun clearUser() = usersQueries.deleteUser()

}