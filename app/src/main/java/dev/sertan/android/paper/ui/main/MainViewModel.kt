package dev.sertan.android.paper.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Single
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val noteRepo: NoteRepo
) : ViewModel() {

    val currentUser = userRepo.currentUser
    private var lastDeletedNote: Note? = null
    private var refreshJob: Job? = null

    private var _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun refreshUser() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch { userRepo.refreshCurrentUser() }
    }

    fun logOut() {
        viewModelScope.launch { userRepo.logOut() }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            val response = noteRepo.delete(note)
            if (response.isSuccess) lastDeletedNote = note

            _uiState.update {
                it.copy(
                    noteDeleted = Single(response.isSuccess),
                    message = Single(response.exception?.localizedMessage)
                )
            }
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            val response = noteRepo.create(lastDeletedNote ?: return@launch)
            if (response.isSuccess) lastDeletedNote = null

            _uiState.update { it.copy(message = Single(response.exception?.localizedMessage)) }
        }
    }
}
