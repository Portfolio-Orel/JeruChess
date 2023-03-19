package com.orels.jeruchess.core.domain.interactors

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.User

interface UserInteractor {
    suspend fun getCachedUser(): User?
    suspend fun getUser(): CommonFlow<User?>
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)

}