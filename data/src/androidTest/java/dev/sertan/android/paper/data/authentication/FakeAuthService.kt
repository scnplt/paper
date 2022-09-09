package dev.sertan.android.paper.data.authentication

import dev.sertan.android.paper.data.model.NetworkUser
import java.util.UUID

internal class FakeAuthService : AuthService {
    private val registeredUsers = mutableListOf<NetworkUser>()
    private var currentUser: NetworkUser? = null

    override suspend fun getCurrentUser(): NetworkUser? = currentUser

    override suspend fun register(email: String, password: String): Boolean {
        registeredUsers.find { it.email == email }?.let { return false }
        return registeredUsers.add(
            NetworkUser(
                uid = UUID.randomUUID().toString(),
                email = email,
                password = password
            )
        )
    }

    override suspend fun logIn(email: String, password: String): Boolean {
        val user = registeredUsers.find { it.email == email && it.password == password }
        currentUser = user ?: return false
        return true
    }

    override suspend fun logOut(): Boolean {
        if (currentUser == null) return false
        currentUser = null
        return true
    }

    override suspend fun deleteAccount(): Boolean {
        if (currentUser == null) return false
        return registeredUsers.find { it == currentUser }?.let {
            currentUser = null
            registeredUsers.remove(it)
        } != null
    }

    override suspend fun sendPasswordResetEmail(email: String): Boolean {
        registeredUsers.find { it.email == email } ?: return false
        return true
    }
}
