package dev.sertan.android.paper.ui.editnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.util.Single
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditNoteViewModel @Inject constructor(private val noteRepo: NoteRepo) : ViewModel() {
    private val _uiState = MutableStateFlow(EditNoteUiState())
    val uiState = _uiState.asStateFlow()

    private var updateJob: Job? = null

    fun setNote(note: Note) = _uiState.update { it.copy(note = note) }

    fun update() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            val note = uiState.value.note
            note.updateDate = System.currentTimeMillis()

            val response = noteRepo.update(note)
            _uiState.update {
                it.copy(
                    isUpdated = response.isSuccess,
                    message = Single(response.exception?.localizedMessage)
                )
            }
        }
    }
}
