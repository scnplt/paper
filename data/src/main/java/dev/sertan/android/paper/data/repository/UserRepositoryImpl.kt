package dev.sertan.android.paper.data.repository

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.mapper.toUserDto
import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.model.Password
import dev.sertan.android.paper.domain.model.UserDto
import dev.sertan.android.paper.domain.repository.UserRepository

class UserRepositoryImpl(private val authService: AuthService) : UserRepository {

    override suspend fun getCurrentUser(): Result<UserDto?> {
        return authService.getCurrentUser().map { networkUser -> networkUser.toUserDto() }
    }

    override suspend fun register(email: Email, password: Password): Result<Boolean> {
        return authService.register(email.toString(), password.toString())
    }

    override suspend fun logIn(email: Email, password: Password): Result<Boolean> {
        return authService.logIn(email.toString(), password.toString())
    }

    override suspend fun logOut(): Result<Boolean> = authService.logOut()

    override suspend fun deleteAccount(): Result<Boolean> = authService.deleteAccount()

    override suspend fun sendPasswordResetEmail(email: Email): Result<Boolean> {
        return authService.sendPasswordResetEmail(email.toString())
    }
}
