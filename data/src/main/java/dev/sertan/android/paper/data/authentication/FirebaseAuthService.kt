package dev.sertan.android.paper.data.authentication

import com.google.firebase.auth.FirebaseAuth
import dev.sertan.android.paper.common.util.tryOrGetDefault
import dev.sertan.android.paper.data.mapper.toNetworkUser
import dev.sertan.android.paper.data.model.NetworkUser
import kotlinx.coroutines.tasks.await

internal class FirebaseAuthService(private val auth: FirebaseAuth) : AuthService {

    override suspend fun getCurrentUser(): NetworkUser? = tryOrGetDefault(null) {
        auth.currentUser.toNetworkUser()
    }

    override suspend fun register(email: String, password: String): Boolean {
        return tryOrGetDefault(false) {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        }
    }

    override suspend fun logIn(email: String, password: String): Boolean = tryOrGetDefault(false) {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result != null
    }

    override suspend fun logOut(): Boolean {
        if (auth.currentUser == null) return false
        return tryOrGetDefault(false) {
            auth.signOut()
            true
        }
    }

    override suspend fun deleteAccount(): Boolean = tryOrGetDefault(false) {
        auth.currentUser?.delete()?.await()
        true
    }

    override suspend fun sendPasswordResetEmail(email: String): Boolean = tryOrGetDefault(false) {
        auth.sendPasswordResetEmail(email).await()
        true
    }
}
