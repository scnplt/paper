package dev.sertan.android.paper.data.auth

import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.PaperException
import dev.sertan.android.paper.data.util.Response

class FakeAuthService : AuthService {
    private var user: User? = null
    private var currentUser: User? = null

    override suspend fun currentUser(): Response<User> {
        return try {
            if (currentUser == null) throw PaperException.UserNotFound
            Response.success(currentUser)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun register(email: String, password: String): Response<Unit> {
        return try {
            if (email == user?.email) throw PaperException.UserAlreadyExists
            user = User(email = email, password = password)
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Response<Unit> {
        return try {
            if (email != user?.email || password != user?.password) throw PaperException.IncorrectInformation
            currentUser = User(email = email, password = password)
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logOut(): Response<Unit> {
        return try {
            if (currentUser == null) throw PaperException.UserNotFound
            currentUser = null
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun deleteAccount(): Response<Unit> {
        return try {
            if (currentUser == null || user == null) throw PaperException.UserNotFound
            user = null
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun sendResetPasswordMail(email: String): Response<Unit> {
        return try {
            if (email != user?.email) throw PaperException.UserNotFound
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

}
