package dev.sertan.android.paper.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.Response
import kotlinx.coroutines.tasks.await

class FirebaseAuthService : AuthService() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun currentUser(): Response<User> {
        return try {
            val user = auth.currentUser?.let { User(it.uid, it.email ?: "") }
            Response.success(user)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun register(email: String, password: String): Response<Boolean> {
        return try {
            validateEmail(email)
            validatePassword(password)

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user?.also { auth.signOut() }
            Response.success(firebaseUser != null)
        } catch (e: FirebaseAuthUserCollisionException) {
            Response.error(AuthException.UserAlreadyExist)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Response<Boolean> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user == null) throw AuthException.IncorrectInformation
            Response.success(true)
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.error(AuthException.IncorrectInformation)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun logOut(): Response<Boolean> {
        return try {
            val result = currentUser().value?.also { auth.signOut() }
            Response.success(result != null)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun deleteAccount(): Response<Boolean> {
        return try {
            val firebaseUser = auth.currentUser?.apply { delete().await() }
            Response.success(firebaseUser != null)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun sendResetPasswordMail(email: String): Response<Boolean> {
        return try {
            validateEmail(email)

            auth.sendPasswordResetEmail(email).await()
            Response.success(true)
        } catch (e: FirebaseAuthInvalidUserException) {
            Response.error(AuthException.UserNotFound)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

}
