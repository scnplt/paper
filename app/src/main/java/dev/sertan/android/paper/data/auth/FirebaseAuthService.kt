package dev.sertan.android.paper.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.util.PaperException
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.tasks.await

internal class FirebaseAuthService : AuthService {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun currentUser(): Response<User> {
        return try {
            val firebaseUser = auth.currentUser ?: throw PaperException.UserNotFound
            val user = User(firebaseUser.uid, firebaseUser.email ?: "")
            Response.success(user)
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: Exception) {
            Response.failure(PaperException.Default)
        }
    }

    override suspend fun register(email: String, password: String): Response<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.signOut()
            Response.success()
        } catch (e: FirebaseAuthUserCollisionException) {
            Response.failure(PaperException.UserAlreadyExists)
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: Exception) {
            Response.failure(PaperException.Default)
        }
    }

    override suspend fun logIn(email: String, password: String): Response<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user == null) throw PaperException.IncorrectInformation
            Response.success()
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.failure(PaperException.IncorrectInformation)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Response.failure(PaperException.IncorrectInformation)
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: Exception) {
            Response.failure(PaperException.Default)
        }
    }

    override suspend fun logOut(): Response<Unit> {
        return try {
            auth.currentUser ?: throw PaperException.UserNotFound
            auth.signOut()
            Response.success()
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: Exception) {
            Response.failure(PaperException.Default)
        }
    }

    override suspend fun deleteAccount(): Response<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw PaperException.UserNotFound
            currentUser.delete().await()
            auth.signOut()
            Response.success()
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: Exception) {
            Response.failure(PaperException.Default)
        }
    }

    override suspend fun sendResetPasswordEmail(email: String): Response<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Response.success()
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.failure(PaperException.UserNotFound)
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: Exception) {
            Response.failure(PaperException.Default)
        }
    }

}
