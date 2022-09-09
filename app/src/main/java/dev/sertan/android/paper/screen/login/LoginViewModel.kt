package dev.sertan.android.paper.screen.login

import android.text.Editable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.common.util.validateEmailAddress
import dev.sertan.android.paper.common.util.validatePassword
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
internal class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState get() = _loginUiState.asStateFlow()


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
}
