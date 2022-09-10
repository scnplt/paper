package dev.sertan.android.paper.domain.repository

import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.model.Password
import dev.sertan.android.paper.domain.model.UserDto

interface AuthRepository {
    suspend fun getLoggedInUser(): Result<UserDto?>
    suspend fun register(email: Email, password: Password): Result<Boolean>
    suspend fun logIn(email: Email, password: Password): Result<Boolean>
    suspend fun logOut(): Result<Boolean>
    suspend fun deleteAccount(): Result<Boolean>
    suspend fun sendPasswordResetEmail(email: Email): Result<Boolean>
}
