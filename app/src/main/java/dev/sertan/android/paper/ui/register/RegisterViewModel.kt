package dev.sertan.android.paper.ui.register

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
internal class RegisterViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirmation = MutableLiveData("")

    val isRegisterButtonEnable: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(email) { value = validateForm() }
        addSource(password) { value = validateForm() }
        addSource(passwordConfirmation) { value = validateForm() }
    }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun register(view: View) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val response = userRepo.register(email.value!!, password.value!!)
            _isLoading.postValue(false)

            if (response.isFailure()) {
                val message = response.exception?.getUIMessage(view.context)
                view.context.showToast(message)
                return@launch
            }

            val message = view.context.getString(R.string.registration_successful)
            view.context.showToast(message)
            view.findNavController().popBackStack()
        }
    }

    private fun validateForm(): Boolean {
        val email = email.value!!
        val password = password.value!!
        val passwordConfirmation = passwordConfirmation.value!!

        if (password != passwordConfirmation) return false
        return Validate.email(email) && Validate.password(password)
    }

}
