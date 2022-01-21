package dev.sertan.android.paper.data.authentication

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.tasks.await

internal class FirebaseAuthService(private val auth: FirebaseAuth) : AuthService {

    override suspend fun currentUser(): Response<User?> = try {
        val user = auth.currentUser?.let { User(uid = it.uid, email = it.email ?: "") }
        Response.success(user)
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun register(email: String, password: String): Response<Unit> = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        auth.signOut()
        Response.success()
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun logIn(email: String, password: String): Response<Boolean> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        Response.success(result.user != null)
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun logOut(): Response<Unit> = try {
        auth.signOut()
        Response.success()
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun deleteAccount(): Response<Unit> = try {
        auth.currentUser?.delete()?.await()?.also { auth.signOut() }
        Response.success()
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun sendResetPasswordEmail(email: String): Response<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Response.success()
    } catch (e: FirebaseException) {
        Response.failure(e)
    }
}
