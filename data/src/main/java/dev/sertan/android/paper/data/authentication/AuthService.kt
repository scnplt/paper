package dev.sertan.android.paper.data.authentication

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.data.model.NetworkUser

interface AuthService {
    suspend fun getCurrentUser(): Result<NetworkUser?>
    suspend fun register(email: String, password: String): Result<Boolean>
    suspend fun logIn(email: String, password: String): Result<Boolean>
    suspend fun logOut(): Result<Boolean>
    suspend fun deleteAccount(): Result<Boolean>
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean>
}
