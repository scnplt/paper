package dev.sertan.android.paper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Single
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    userRepo: UserRepo,
    private val noteRepo: NoteRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private var lastDeletedNote: Note? = null

    init {
        viewModelScope.launch {
            val user = userRepo.currentUser.value
            val userUid = user.value?.uid ?: return@launch

            noteRepo.getNotes(userUid).collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun deleteNote(position: Int) {
        val notes = uiState.value.notes
        val note = notes.value?.get(position) ?: return

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

            _uiState.update {
                it.copy(message = Single(response.exception?.localizedMessage))
            }
        }
    }

}
