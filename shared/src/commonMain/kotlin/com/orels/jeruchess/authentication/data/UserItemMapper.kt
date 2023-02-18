package com.orels.jeruchess.authentication.data

import com.orels.jeruchess.authentication.domain.model.User
import database.UserEntity

fun UserEntity.toUser(): User = User(
    id = id,
    lastName = lastName,
    email = email,
)

fun User.toUserEntity(): UserEntity = UserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phoneNumber
)