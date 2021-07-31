package dev.sertan.android.paper.data.repo

import androidx.core.util.PatternsCompat
import dev.sertan.android.paper.data.auth.AuthService
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.util.PaperException
import dev.sertan.android.paper.data.util.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Singleton
class UserRepo @Inject constructor(private val authService: AuthService) {
    private val _currentUser = MutableStateFlow<Response<User>>(Response.idle())
    val currentUser: StateFlow<Response<User>> get() = _currentUser

    suspend fun refreshUser() {
        _currentUser.emit(Response.loading())
        val response = authService.currentUser()
        _currentUser.emit(response)
    }

    fun register(email: String, password: String): Flow<Response<Unit>> {
        return flow {
            emit(Response.loading())
            validateEmailAddress(email)
            validatePassword(password)
            val response = authService.register(email, password)
            authService.logOut()
            emit(response)
        }.catch { e ->
            emit(Response.error(e as Exception))
        }.flowOn(Dispatchers.IO)
    }

    fun logIn(email: String, password: String): Flow<Response<Unit>> {
        return flow {
            emit(Response.loading())
            validateEmailAddress(email)
            validatePassword(password)
            val response = authService.logIn(email, password)
            refreshUser()
            emit(response)
        }.catch { e ->
            emit(Response.error(e as Exception))
        }.flowOn(Dispatchers.IO)
    }

    fun logOut(): Flow<Response<Unit>> {
        return flow {
            emit(Response.loading())
            val response = authService.logOut()
            refreshUser()
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteAccount(): Flow<Response<Unit>> {
        return flow {
            emit(Response.loading())
            val response = authService.deleteAccount()
            authService.logOut()
            refreshUser()
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun sendResetPasswordMail(email: String): Flow<Response<Unit>> {
        return flow {
            emit(Response.loading())
            validateEmailAddress(email)
            val response = authService.sendResetPasswordMail(email)
            emit(response)
        }.catch { e ->
            emit(Response.error(e as Exception))
        }.flowOn(Dispatchers.IO)
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
