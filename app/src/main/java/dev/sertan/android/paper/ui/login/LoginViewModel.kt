package dev.sertan.android.paper.ui.login

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Validate
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    val isLoginButtonEnable: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(email) { value = validateForm() }
        addSource(password) { value = validateForm() }
    }

    fun login(view: View) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val response = userRepo.logIn(email.value!!, password.value!!)
            _isLoading.postValue(false)

            if (response.isFailure()) view.context.showToast(response.exception?.messageRes)
        }
    }

    fun register(view: View) {
        val direction = LoginFragmentDirections.actionLoginToRegister()
        view.findNavController().navigate(direction)
    }

    fun recoverPassword(view: View) {
        val email = email.value!!
        if (!Validate.email(email)) {
            view.context.showToast(R.string.invalid_email_message)
            return
        }

        viewModelScope.launch {
            userRepo.sendResetPasswordEmail(email)

            // Showing the "User Not Found" exception message can be a security issue.
            view.context.showToast(R.string.send_password_recovery_email)
        }
    }

    private fun validateForm(): Boolean {
        return Validate.email(email.value!!) && Validate.password(password.value!!)
    }

}
