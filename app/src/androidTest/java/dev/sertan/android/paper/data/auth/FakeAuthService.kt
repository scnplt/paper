package dev.sertan.android.paper.data.auth

import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.Response
import java.util.*

class FakeAuthService : AuthService() {
    private var user: User? = null
    private var currentUser: User? = null

    override suspend fun register(email: String, password: String): Response<User> {
        if (user != null) return Response.error(AuthException.UserAlreadyExist)

        try {
            validateEmail(email)
            validatePassword(password)
        } catch (e: Exception) {
            return Response.error(e)
        }

        return User(UUID.randomUUID().toString(), email, password).also { user = it }
            .let { Response.success(it) }
    }

    override suspend fun currentUser(): Response<User> {
        if (currentUser == null) return Response.error(AuthException.UserNotFound)
        return Response.success(currentUser)
    }

    override suspend fun logIn(email: String, password: String): Response<User> {
        currentUser?.let { return Response.success(it) }

        try {
            validateEmail(email)
            validatePassword(password)
        } catch (e: Exception) {
            return Response.error(e)
        }

        if (email == user?.email && password == user?.password) {
            currentUser = user
            return Response.success(user)
        }

        return Response.error(AuthException.IncorrectInformation)
    }

    override suspend fun logOut(): Response<Boolean> {
        if (currentUser == null) return Response.error(AuthException.UserNotFound)
        currentUser = null
        return Response.success(true)
    }

    override suspend fun deleteAccount(): Response<Boolean> {
        if (user == null) return Response.error(AuthException.UserNotFound)
        user = null
        return logOut()
    }

    override suspend fun sendResetPasswordMail(email: String): Response<Boolean> {
        if (user?.email != email) return Response.error(AuthException.UserNotFound)
        try {
            validateEmail(email)
        } catch (e: Exception) {
            return Response.error(e)
        }

        return Response.success(true)
    }
}
