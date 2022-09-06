package dev.sertan.android.paper.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.domain.usecase.GetCurrentUserUidUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase
) : ViewModel() {

    private val _currentUserUid = MutableStateFlow<String?>(null)
    val currentUserUid get() = _currentUserUid.asStateFlow()

    init {
        updateCurrentUserId()
    }

    private fun updateCurrentUserId() {
        viewModelScope.launch {
            val userUidResult = getCurrentUserUidUseCase()
            _currentUserUid.update { userUidResult.data }
        }
    }
}
