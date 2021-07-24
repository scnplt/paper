package dev.sertan.android.paper.data.auth

import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.Response

class FakeAuthService : AuthService() {
    private var user: User? = null
    private var currentUser: User? = null

    override suspend fun currentUser(): Response<User> {
        return try {
            if (currentUser == null) throw AuthException.UserNotFound
            Response.success(currentUser)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun register(email: String, password: String): Response<Boolean> {
        return try {
            validateEmail(email)
            validatePassword(password)
            if (email == user?.email) throw AuthException.UserAlreadyExist
            user = User(email = email, password = password)
            Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Response<Boolean> {
        return try {
            validateEmail(email)
            validatePassword(password)
            if (email != user?.email || password != user?.password) throw AuthException.IncorrectInformation
            currentUser = User(email = email, password = password)
            Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logOut(): Response<Boolean> {
        if (currentUser == null) return Response.success(false)
        return Response.success(true).also { currentUser = null }
    }

    override suspend fun deleteAccount(): Response<Boolean> {
        if (user == null) return Response.success(false)
        return Response.success(true).also { user = null }
    }

    override suspend fun sendResetPasswordMail(email: String): Response<Boolean> {
        return try {
            validateEmail(email)
            if (email != user?.email) throw AuthException.UserNotFound
            Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

}
