package dev.sertan.android.paper.data.repo

import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.util.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Singleton
internal class UserRepo @Inject constructor(private val authService: AuthService) {
    private val _currentUser = MutableStateFlow<Response<User?>>(Response.idle())
    val currentUser: StateFlow<Response<User?>> get() = _currentUser

    suspend fun refreshCurrentUser() {
        val response = authService.currentUser()
        _currentUser.emit(response)
    }

    suspend fun register(email: String, password: String): Response<Unit> =
        authService.register(email, password)

    suspend fun logIn(email: String, password: String): Response<Boolean> =
        authService.logIn(email, password).also { refreshCurrentUser() }

    suspend fun logOut(): Response<Unit> = authService.logOut().also { refreshCurrentUser() }

    suspend fun deleteAccount(): Response<Unit> =
        authService.deleteAccount().also { refreshCurrentUser() }

    suspend fun sendResetPasswordEmail(email: String): Response<Unit> =
        authService.sendResetPasswordEmail(email)

}
