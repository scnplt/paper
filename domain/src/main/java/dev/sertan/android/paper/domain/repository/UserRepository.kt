package dev.sertan.android.paper.domain.repository

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.model.Password
import dev.sertan.android.paper.domain.model.UserDto

interface UserRepository {
    suspend fun getCurrentUser(): Result<UserDto?>
    suspend fun register(email: Email, password: Password): Result<Boolean>
    suspend fun logIn(email: Email, password: Password): Result<Boolean>
    suspend fun logOut(): Result<Boolean>
    suspend fun deleteAccount(): Result<Boolean>
    suspend fun sendPasswordResetEmail(email: Email): Result<Boolean>
}
