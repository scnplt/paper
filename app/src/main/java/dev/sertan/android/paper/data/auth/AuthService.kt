package dev.sertan.android.paper.data.auth

import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.util.Response

internal interface AuthService {
    suspend fun currentUser(): Response<User>
    suspend fun register(email: String, password: String): Response<Unit>
    suspend fun logIn(email: String, password: String): Response<Unit>
    suspend fun logOut(): Response<Unit>
    suspend fun deleteAccount(): Response<Unit>
    suspend fun sendResetPasswordEmail(email: String): Response<Unit>
}
