package dev.sertan.android.paper.screen.login

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.common.util.OneTimeState
import dev.sertan.android.paper.common.util.validateEmailAddress
import dev.sertan.android.paper.common.util.validatePassword
import dev.sertan.android.paper.domain.usecase.GetCurrentUserUidUseCase
import dev.sertan.android.paper.domain.usecase.LogInUseCase
import dev.sertan.android.paper.domain.usecase.SendPasswordResetEmailUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase,
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase,
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState get() = _loginUiState.asStateFlow()

    init {
        updateUiStateByLoginStatus()
    }

    private fun updateUiStateByLoginStatus() {
        runWithLoadingState {
            val currentUserUidResult = getCurrentUserUidUseCase()
            _loginUiState.update {
                it.copy(isLoggedIn = !currentUserUidResult.data.isNullOrBlank())
            }
        }
    }

    fun updateEmailAddress(address: Editable?) {
        val emailAddress = address.toString().trim()
        _loginUiState.update {
            it.copy(
                emailAddress = emailAddress,
                isEmailValid = validateEmailAddress(emailAddress)
            )
        }
    }

    fun updatePassword(pwd: Editable?) {
        val password = pwd.toString().trim()
        _loginUiState.update {
            it.copy(
                password = password,
                isPasswordValid = validatePassword(password)
            )
        }
    }

    fun logIn() {
        with(loginUiState.value) {
            if (isEmailValid != true || isPasswordValid != true) return
            runWithLoadingState {
                val logInResult = logInUseCase(email = emailAddress, password = password)
                _loginUiState.update {
                    it.copy(
                        isLoggedIn = logInResult.data == true,
                        exceptionMessageRes = OneTimeState(data = logInResult.exception?.messageRes)
                    )
                }
            }
        }
    }

    fun sendPasswordResetEmail() {
        with(loginUiState.value) {
            if (isEmailValid != true) return
            runWithLoadingState {
                val result = sendPasswordResetEmailUseCase(email = emailAddress)
                _loginUiState.update {
                    it.copy(
                        isPasswordResetEmailSendSuccess = OneTimeState(data = result.data),
                        exceptionMessageRes = OneTimeState(data = result.exception?.messageRes)
                    )
                }
            }
        }
    }

    private inline fun runWithLoadingState(crossinline block: suspend () -> Unit) {
        if (loginUiState.value.isLoading) return
        _loginUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            block()
            _loginUiState.update { it.copy(isLoading = false) }
        }
    }
}
