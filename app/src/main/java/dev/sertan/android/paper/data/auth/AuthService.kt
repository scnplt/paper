package dev.sertan.android.paper.data.auth

import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.Response
import dev.sertan.android.paper.data.util.Validator

abstract class AuthService {
    abstract suspend fun currentUser(): Response<User>
    abstract suspend fun register(email: String, password: String): Response<Boolean>
    abstract suspend fun logIn(email: String, password: String): Response<Boolean>
    abstract suspend fun logOut(): Response<Boolean>
    abstract suspend fun deleteAccount(): Response<Boolean>
    abstract suspend fun sendResetPasswordMail(email: String): Response<Boolean>

    @Throws(AuthException.InvalidEmail::class)
    fun validateEmail(email: String) {
        if (Validator.validateEmail(email)) return
        throw AuthException.InvalidEmail
    }

    @Throws(AuthException.InvalidPassword::class)
    fun validatePassword(password: String) {
        if (Validator.validatePassword(password)) return
        throw AuthException.InvalidPassword
    }
}
