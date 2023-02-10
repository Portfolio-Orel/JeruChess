package com.orels.jeruchess.authentication.data

import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import com.orels.jeruchess.authentication.domain.model.User
import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.database.JeruChessDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.map

class SqlDelightAuthDataSource(
    db: JeruChessDatabase,
) : AuthDataSource {

    private val queries = db.jeruchessQueries
    override fun getUser(): CommonFlow<User> = queries.getUser()
        .asFlow()
        .mapToOne()
        .map { userEntity -> userEntity.toUser() }
        .toCommonFlow()

    override suspend fun addUser(user: User) = queries.insertUser(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        phone = user.phone
    )

    override suspend fun updateUser(user: User) = Unit

    override suspend fun deleteUser(user: User) = queries.deleteUser(user.id)


}