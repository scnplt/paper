package dev.sertan.android.paper.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.PaperException
import dev.sertan.android.paper.data.util.Response
import kotlinx.coroutines.tasks.await

class FirebaseAuthService : AuthService {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun currentUser(): Response<User> {
        return try {
            val firebaseUser = auth.currentUser ?: throw PaperException.UserNotFound
            val user = User(firebaseUser.uid, firebaseUser.email ?: "")
            Response.success(user)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun register(email: String, password: String): Response<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.success()
        } catch (e: FirebaseAuthUserCollisionException) {
            Response.error(PaperException.UserAlreadyExists)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Response<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user == null) throw PaperException.IncorrectInformation
            Response.success()
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.error(PaperException.IncorrectInformation)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Response.error(PaperException.IncorrectInformation)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logOut(): Response<Unit> {
        return try {
            auth.currentUser ?: throw PaperException.UserNotFound
            auth.signOut()
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun deleteAccount(): Response<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw PaperException.UserNotFound
            currentUser.delete().await()
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun sendResetPasswordMail(email: String): Response<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Response.success()
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.error(PaperException.UserNotFound)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

}
