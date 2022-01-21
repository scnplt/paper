package dev.sertan.android.paper.ui.addnote

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
internal class AddNoteViewModel @Inject constructor(
    userRepo: UserRepo,
    private val noteRepo: NoteRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddNoteUiState())
    val uiState = _uiState.asStateFlow()

    private val userUid: String? = userRepo.currentUser.value.run { value?.uid }

    private var saveJob: Job? = null

    fun save() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            val note = Note(
                title = uiState.value.title,
                content = uiState.value.content,
                userUid = userUid ?: return@launch
            )

            val response = noteRepo.create(note)
            _uiState.update {
                it.copy(
                    isSaved = response.isSuccess,
                    message = Single(response.exception?.localizedMessage)
                )
            }
        }
    }
}
