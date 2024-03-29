package com.orels.jeruchess.main.data.users

import com.orels.jeruchess.main.domain.model.Gender
import com.orels.jeruchess.main.domain.model.User
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IsUserRegisteredDto(
    @SerialName("is_registration_completed") val isRegistered: Boolean
)

@kotlinx.serialization.Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("first_name") val firstName: String = "",
    @SerialName("last_name") val lastName: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("gender") val gender: String = "MALE",
    @SerialName("phone_number") val phoneNumber: String = "",
    @SerialName("player_number") val playerNumber: String? = "",
    @SerialName("date_of_birth") val dateOfBirth: Long? = 0L,
) {
    fun toUser(): User = User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        gender = Gender.fromString(gender),
        phoneNumber = phoneNumber,
        playerNumber = playerNumber ?: "",
        dateOfBirth = dateOfBirth ?: 0L
    )
}

fun User.toUserDto(): UserDto = UserDto(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    gender = gender.name,
    phoneNumber = phoneNumber,
    playerNumber = playerNumber,
    dateOfBirth = dateOfBirth,
)