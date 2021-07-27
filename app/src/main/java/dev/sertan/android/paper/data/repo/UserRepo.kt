package dev.sertan.android.paper.data.repo

import androidx.core.util.PatternsCompat
import dev.sertan.android.paper.data.auth.AuthService
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.PaperException
import dev.sertan.android.paper.data.util.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Singleton
class UserRepo @Inject constructor(private val authService: AuthService) {
    private val _currentUser = MutableStateFlow<Response<User>>(Response.idle())
    val currentUser: StateFlow<Response<User>> get() = _currentUser

    init {
        CoroutineScope(Dispatchers.Default).launch { refreshCurrentUser() }
    }

    suspend fun register(email: String, password: String): Response<Unit> {
        return try {
            validateEmailAddress(email)
            validatePassword(password)
            authService.register(email, password)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    suspend fun logIn(email: String, password: String): Response<Unit> {
        return try {
            validateEmailAddress(email)
            validatePassword(password)
            authService.logIn(email, password).also { refreshCurrentUser() }
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    suspend fun logOut(): Response<Unit> {
        return authService.logOut().also { refreshCurrentUser() }
    }

    suspend fun deleteAccount(): Response<Unit> {
        return authService.deleteAccount().also { logOut() }
    }

    suspend fun sendResetPasswordMail(email: String): Response<Unit> {
        return try {
            validateEmailAddress(email)
            authService.sendResetPasswordMail(email)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    private suspend fun refreshCurrentUser() {
        val user = authService.currentUser()
        _currentUser.emit(user)
    }

    private fun validateEmailAddress(email: String) {
        if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) return
        throw PaperException.InvalidEmailAddress
    }

    private fun validatePassword(password: String) {
        if (password.length >= 8) return
        throw PaperException.InvalidPassword
    }
}
