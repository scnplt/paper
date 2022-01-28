package dev.sertan.android.paper.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.User
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private var job: Job? = null
    val currentUser: StateFlow<Response<User?>> = userRepo.currentUser

    fun refreshUser() {
        job?.cancel()
        job = viewModelScope.launch { userRepo.refreshCurrentUser() }
    }

    fun logOut() {
        viewModelScope.launch { userRepo.logOut() }
    }
}
