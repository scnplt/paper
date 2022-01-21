package dev.sertan.android.paper.ui.register

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

@HiltViewModel
internal class RegisterViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()
    private var registerJob: Job? = null

    fun register() {
        registerJob?.cancel()
        val email = uiState.value.email
        val password = uiState.value.password

        _uiState.update { it.copy(isLoading = true) }
        registerJob = viewModelScope.launch {
            userRepo.register(email = email, password = password).run {
                _uiState.update { it.copy(isLoading = false, isRegistered = isSuccess) }
                _uiState.update {
                    when (isSuccess) {
                        true -> it.copy(messageRes = Single(R.string.registration_successful))
                        else -> it.copy(message = Single(exception?.localizedMessage))
                    }
                }
            }
        }
    }
}
