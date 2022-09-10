package dev.sertan.android.paper.data.repository

import dev.sertan.android.paper.common.util.tryGetResult
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.mapper.toUserDto
import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.model.Password
import dev.sertan.android.paper.domain.model.UserDto
import dev.sertan.android.paper.domain.repository.AuthRepository

internal class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {

    override suspend fun getLoggedInUser(): Result<UserDto?> = tryGetResult {
        authService.getCurrentUser().toUserDto()
    }

    override suspend fun register(email: Email, password: Password): Result<Boolean> {
        return tryGetResult {
            authService.register(email = Email.toString(), password = password.toString())
        }
    }

    override suspend fun logIn(email: Email, password: Password): Result<Boolean> = tryGetResult {
        authService.logIn(email = email.toString(), password = password.toString())
    }

    override suspend fun logOut(): Result<Boolean> = tryGetResult { authService.logOut() }

    override suspend fun deleteAccount(): Result<Boolean> = tryGetResult {
        authService.deleteAccount()
    }

    override suspend fun sendPasswordResetEmail(email: Email): Result<Boolean> = tryGetResult {
        authService.sendPasswordResetEmail(email = email.toString())
    }
}
