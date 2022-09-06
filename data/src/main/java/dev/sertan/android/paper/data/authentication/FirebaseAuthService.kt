package dev.sertan.android.paper.data.authentication

import com.google.firebase.auth.FirebaseAuth
import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.common.util.tryGetResult
import dev.sertan.android.paper.data.mapper.toNetworkUser
import dev.sertan.android.paper.data.model.NetworkUser
import kotlinx.coroutines.tasks.await

internal class FirebaseAuthService(private val auth: FirebaseAuth) : AuthService {

    override suspend fun getCurrentUser(): Result<NetworkUser?> {
        return tryGetResult { auth.currentUser.toNetworkUser() }
    }

    override suspend fun register(email: String, password: String): Result<Boolean> {
        return tryGetResult { auth.createUserWithEmailAndPassword(email, password).isSuccessful }
    }

    override suspend fun logIn(email: String, password: String): Result<Boolean> {
        return tryGetResult {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user != null
        }
    }

    override suspend fun logOut(): Result<Boolean> {
        return tryGetResult {
            auth.signOut()
            true
        }
    }

    override suspend fun deleteAccount(): Result<Boolean> {
        return tryGetResult { auth.currentUser?.delete()?.isSuccessful ?: false }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return tryGetResult { auth.sendPasswordResetEmail(email).isSuccessful }
    }
}
