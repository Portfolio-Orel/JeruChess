package com.orels.jeruchess.android.presentation.auth.register

import androidx.annotation.StringRes
import com.orels.jeruchess.main.domain.model.Gender
import com.orels.jeruchess.main.domain.model.User

enum class Stage {
    BASIC_INFORMATION, EMAIL_NUMBER, CONFIRMATION, DONE, ERROR
}

// this will show the stages order, not as a string
val registrationProcess: List<Stage> = listOf(
    Stage.EMAIL_NUMBER,
//    Stage.CONFIRMATION,
    Stage.BASIC_INFORMATION,
    Stage.DONE
)

fun Stage.next(): Stage {
    val nextIndex = registrationProcess.indexOf(this) + 1
    return if (nextIndex < registrationProcess.size) {
        registrationProcess[nextIndex]
    } else {
        this
    }
}

fun Stage.previous(): Stage {
    val previousIndex = registrationProcess.indexOf(this) - 1
    return if (previousIndex >= 0) {
        registrationProcess[previousIndex]
    } else {
        this
    }
}

data class RegisterState(
    val id: String = "",
    val username: String = "orelz",
    val password: String = "00220022",
    val confirmPassword: String = "00220022",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "thischessapp@gmail.com",
    val gender: Gender = Gender.None,
    val phoneNumber: String = "0543056286",
    val playerNumber: String = "", // TODO
    val dateOfBirth: Long = 0L,
    val code: String = "",

    val user: User = User(),

    val stage: Stage = registrationProcess[0],
    val isConfirmationOnly: Boolean = false,
    val isLoading: Boolean = false,

    @StringRes val error: Int = 0

    )