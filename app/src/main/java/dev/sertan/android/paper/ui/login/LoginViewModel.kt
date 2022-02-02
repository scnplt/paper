package dev.sertan.android.paper.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Single
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val EMAIL_DELAY_MS = 8000L

@HiltViewModel
internal class LoginViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private var logInJob: Job? = null
    private var lastEmailSentTimeMs = 0L

    fun logIn() {
        logInJob?.cancel()
        val email = uiState.value.email
        val password = uiState.value.password

        _uiState.update { it.copy(isLoading = true) }
        logInJob = viewModelScope.launch {
            val response = userRepo.logIn(email = email, password = password)
            _uiState.update {
                it.copy(isLoading = false, message = Single(response.exception?.localizedMessage))
            }
        }
    }

    fun recoverPassword() {
        val now = System.currentTimeMillis()
        if ((now - lastEmailSentTimeMs) < EMAIL_DELAY_MS) return

        val email = uiState.value.email
        lastEmailSentTimeMs = now

        viewModelScope.launch {
            val response = userRepo.sendResetPasswordEmail(email)
            _uiState.update {
                when {
                    response.isFailure -> it.copy(message = Single(response.exception?.localizedMessage))
                    else -> it.copy(messageRes = Single(R.string.send_password_recovery_email))
                }
            }
        }
    }
}
