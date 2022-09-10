package dev.sertan.android.paper.data.authentication

import dev.sertan.android.paper.data.model.NetworkUser

interface AuthService {
    suspend fun getCurrentUser(): NetworkUser?
    suspend fun register(email: String, password: String): Boolean
    suspend fun logIn(email: String, password: String): Boolean
    suspend fun logOut(): Boolean
    suspend fun deleteAccount(): Boolean
    suspend fun sendPasswordResetEmail(email: String): Boolean
}
