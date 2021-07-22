package dev.sertan.android.paper.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.Response
import kotlinx.coroutines.tasks.await

class FirebaseAuthService : AuthService() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun register(email: String, password: String): Response<User?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception()
            auth.signOut()
            val user = User(firebaseUser.uid, email, password)
            Response.success(user)
        } catch (e: FirebaseAuthUserCollisionException) {
            Response.error(AuthException.UserAlreadyExist)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun currentUser(): Response<User?> {
        return try {
            val firebaseUser = auth.currentUser ?: throw AuthException.UserNotFound
            val user = User(firebaseUser.uid, firebaseUser.email!!)
            Response.success(user)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Response<User?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw AuthException.IncorrectInformation
            val user = User(firebaseUser.uid, firebaseUser.email!!)
            Response.success(user)
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.error(AuthException.IncorrectInformation)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logOut(): Response<Boolean> {
        return try {
            if (currentUser().isError()) throw AuthException.UserNotFound
            auth.signOut()
            Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun deleteAccount(): Response<Boolean> {
        return try {
            val firebaseUser = auth.currentUser ?: throw AuthException.UserNotFound
            firebaseUser.delete().await()
            Response.success(true)
        } catch (e: AuthException.UserNotFound) {
            Response.error(AuthException.UserNotFound)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun sendResetPasswordMail(email: String): Response<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Response.success(true)
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.error(AuthException.UserNotFound)
        } catch (e: Exception) {
            Response.error(e)
        }
    }
}
