package dev.sertan.android.paper.ui.login

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Validate
import dev.sertan.android.paper.util.showToast
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class LoginViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private var job: Job? = null

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    val isLoginButtonEnable: StateFlow<Boolean> = email.combine(password) { _, _ ->
        validateForm()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = false
    )

    fun login(view: View) {
        job?.cancel()
        job = viewModelScope.launch {
            _isLoading.emit(true)
            val response = userRepo.logIn(email = email.value, password = password.value)
            _isLoading.emit(false)

            view.context.showToast(response.exception?.localizedMessage)
        }
    }

    fun register(view: View) {
        val direction = LoginFragmentDirections.actionLoginToRegister()
        view.findNavController().navigate(direction)
    }

    fun recoverPassword(view: View) {
        if (!Validate.email(email.value)) {
            val message = view.context.getString(R.string.invalid_email_message)
            view.context.showToast(message)
            return
        }

        viewModelScope.launch {
            val response = userRepo.sendResetPasswordEmail(email.value)
            if (response.isSuccess) {
                val message = view.context.getString(R.string.send_password_recovery_email)
                view.context.showToast(message)
            }
            view.context.showToast(response.exception?.localizedMessage)
        }
    }

    private fun validateForm(): Boolean =
        Validate.email(email.value) && Validate.password(password.value)

    companion object {
        private const val STOP_TIMEOUT_MS = 5000L
    }

}
