package dev.sertan.android.paper.data.authentication

import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.util.Response

internal class FakeAuthService : AuthService {
    private var user: User? = null
    private var currentUser: User? = null

    override suspend fun currentUser(): Response<User> = Response.success(currentUser)

    override suspend fun register(email: String, password: String): Response<Unit> = try {
        if (email == user?.email) error("This email address is already in use")
        user = User(email = email, password = password)
        Response.success()
    } catch (e: Exception) {
        Response.failure(e)
    }

    override suspend fun logIn(email: String, password: String): Response<Boolean> = try {
        if (email != user?.email || password != user?.password) error("The email address or password is incorrect!")
        currentUser = user
        Response.success(true)
    } catch (e: Exception) {
        Response.failure(e)
    }

    override suspend fun logOut(): Response<Unit> = try {
        if (currentUser == null) error("User not found!")
        currentUser = null
        Response.success()
    } catch (e: Exception) {
        Response.failure(e)
    }

    override suspend fun deleteAccount(): Response<Unit> = try {
        if (currentUser == null || user == null) error("User not found!")
        user = null
        currentUser = null
        Response.success()
    } catch (e: Exception) {
        Response.failure(e)
    }

    override suspend fun sendResetPasswordEmail(email: String): Response<Unit> = try {
        if (email != user?.email) error("User not found!")
        Response.success()
    } catch (e: Exception) {
        Response.failure(e)
    }
}
