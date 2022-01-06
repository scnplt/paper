package dev.sertan.android.paper.ui.register

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
internal class RegisterViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private var job: Job? = null

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordConfirmation = MutableStateFlow("")

    val isRegisterButtonEnable: StateFlow<Boolean> =
        combine(email, password, passwordConfirmation) { _, _, _ ->
            validateForm()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun register(view: View) {
        job?.cancel()
        job = viewModelScope.launch {
            _isLoading.emit(true)
            val response = userRepo.register(email = email.value, password = password.value)
            _isLoading.emit(false)

            if (response.isSuccess) {
                val message = view.context.getString(R.string.registration_successful)
                view.context.showToast(message)
                view.findNavController().popBackStack()
            }

            view.context.showToast(response.exception?.localizedMessage)
        }
    }

    private fun validateForm(): Boolean = password.value == passwordConfirmation.value
            && Validate.email(email.value) && Validate.password(password.value)

}
