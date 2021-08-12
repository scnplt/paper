package dev.sertan.android.paper.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    val currentUser: LiveData<Response<User>> get() = userRepo.currentUser.asLiveData()

    fun refreshUser() {
        viewModelScope.launch { userRepo.refreshCurrentUser() }
    }

}
