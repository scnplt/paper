package dev.sertan.android.paper.data.authentication

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.common.util.tryGetResult
import dev.sertan.android.paper.data.model.NetworkUser
import java.util.UUID

internal class FakeAuthService : AuthService {

    private val registeredUsers = mutableListOf<NetworkUser>()
    private var currentUser: NetworkUser? = null

    override suspend fun getCurrentUser(): Result<NetworkUser?> {
        return Result.Success(data = currentUser)
    }

    override suspend fun register(email: String, password: String): Result<Boolean> {
        return tryGetResult {
            registeredUsers.find { it.email == email }?.let { error("This email address is already being used") }
            registeredUsers.add(
                NetworkUser(
                    uid = UUID.randomUUID().toString(),
                    email = email,
                    password = password
                )
            )
        }
    }

    override suspend fun logIn(email: String, password: String): Result<Boolean> {
        return tryGetResult {
            val user = registeredUsers.find { it.email == email && it.password == password }
                ?: error("The email address or password is incorrect!")
            currentUser = user
            true
        }
    }

    override suspend fun logOut(): Result<Boolean> {
        return tryGetResult {
            if (currentUser == null) error("User not found!")
            currentUser = null
            true
        }
    }

    override suspend fun deleteAccount(): Result<Boolean> {
        return tryGetResult {
            if (currentUser == null) error("User not found!")
            registeredUsers.find { it == currentUser }?.let {
                currentUser = null
                registeredUsers.remove(it)
            } != null
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return tryGetResult {
            registeredUsers.find { it.email == email } ?: error("User not found!")
            true
        }
    }
}
