package com.orels.jeruchess.main.data.users

import com.orels.jeruchess.main.domain.model.User
import database.UserEntity

fun UserEntity.toUser() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    gender = gender,
    phoneNumber = phoneNumber,
    playerNumber = playerNumber,
    dateOfBirth = dateOfBirth

)