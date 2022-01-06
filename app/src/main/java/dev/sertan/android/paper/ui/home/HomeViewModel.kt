package dev.sertan.android.paper.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Response
import dev.sertan.android.paper.util.showToast
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    userRepo: UserRepo,
    private val noteRepo: NoteRepo
) : ViewModel() {

    private var lastDeletedNote: Note? = null

    val notes: StateFlow<Response<List<Note>?>> = userRepo.currentUser.transform {
        val result = it.value?.run { noteRepo.getNotes(userUid = uid) } ?: return@transform
        emitAll(result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Response.idle()
    )

    val isEmpty: StateFlow<Boolean> = notes.transform {
        emit(it.value.isNullOrEmpty())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    fun delete(view: View, position: Int) {
        val response = notes.value
        val note = response.value?.get(position) ?: return

        viewModelScope.launch {
            if (noteRepo.delete(note).isFailure) {
                view.context.showToast(response.exception?.localizedMessage)
                return@launch
            }
            lastDeletedNote = note
            Snackbar.make(view, R.string.note_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo) { undoDelete(view) }.show()
        }
    }

    private fun undoDelete(view: View) {
        viewModelScope.launch {
            val response = noteRepo.create(lastDeletedNote ?: return@launch)
            if (response.isFailure) {
                view.context.showToast(response.exception?.localizedMessage)
                return@launch
            }
            lastDeletedNote = null
        }
    }

}
