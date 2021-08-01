package dev.sertan.android.paper.data.auth

import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.util.PaperException
import dev.sertan.android.paper.util.Response

internal class FakeAuthService : AuthService {
    private var user: User? = null
    private var currentUser: User? = null

    override suspend fun currentUser(): Response<User> {
        return currentUser
            ?.let { Response.success(it) }
            ?: Response.failure(PaperException.UserNotFound)
    }

    override suspend fun register(email: String, password: String): Response<Unit> {
        if (email == user?.email) return Response.failure(PaperException.UserAlreadyExists)
        user = User(email = email, password = password)
        return Response.success()
    }

    override suspend fun logIn(email: String, password: String): Response<Unit> {
        if (email != user?.email || password != user?.password) {
            return Response.failure(PaperException.IncorrectInformation)
        }
        currentUser = User(email = email, password = password)
        return Response.success()
    }

    override suspend fun logOut(): Response<Unit> {
        if (currentUser == null) return Response.failure(PaperException.UserNotFound)
        currentUser = null
        return Response.success()
    }

    override suspend fun deleteAccount(): Response<Unit> {
        if (currentUser == null || user == null) return Response.failure(PaperException.UserNotFound)
        user = null
        currentUser = null
        return Response.success()
    }

    override suspend fun sendResetPasswordEmail(email: String): Response<Unit> {
        if (email != user?.email) return Response.failure(PaperException.UserNotFound)
        return Response.success()
    }

}
