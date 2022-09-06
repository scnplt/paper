package dev.sertan.android.paper.screen.login

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.common.util.OneTimeState
import dev.sertan.android.paper.common.util.validateEmailAddress
import dev.sertan.android.paper.common.util.validatePassword
import dev.sertan.android.paper.domain.usecase.LogInUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState get() = _loginUiState.asStateFlow()

    // TODO create custom classes for exceptions
    fun logIn() {
        if (!isAllFormValid() || loginUiState.value.isLoading) return
        _loginUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = with(loginUiState.value) {
                logInUseCase(email = emailAddress, password = password)
            }
            _loginUiState.update {
                it.copy(
                    isLoading = false,
                    isLoggedIn = result.data ?: false,
                    exceptionMessage = OneTimeState(data = result.exception?.message)
                )
            }
        }
    }

    fun updateEmailAddress(address: Editable?) {
        _loginUiState.update { it.copy(emailAddress = address.toString()) }
    }

    fun updatePassword(pwd: Editable?) {
        _loginUiState.update { it.copy(password = pwd.toString()) }
    }

    private fun isAllFormValid(): Boolean {
        return with(loginUiState.value) {
            validateEmailAddress(address = emailAddress) && validatePassword(pwd = password)
        }
    }

    fun sendPasswordResetEmail() {
        // TODO send password reset email
    }
}
