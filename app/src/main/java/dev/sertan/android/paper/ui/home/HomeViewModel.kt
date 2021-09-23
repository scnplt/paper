package dev.sertan.android.paper.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Response
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    userRepo: UserRepo,
    private val noteRepo: NoteRepo
) : ViewModel() {

    private var lastDeletedNote: Note? = null

    val notes: LiveData<Response<List<Note>>> =
        Transformations.switchMap(userRepo.currentUser.asLiveData()) {
            it.value?.run { noteRepo.getAllData(this.uid).asLiveData() }
        }

    val isEmpty: LiveData<Boolean> = Transformations
        .map(notes) { it.value?.isNullOrEmpty() }

    fun delete(view: View, position: Int) {
        val note = notes.value?.value?.get(position) ?: return

        viewModelScope.launch {
            val response = noteRepo.delete(note)
            if (response.isFailure()) {
                view.context.showToast(response.exception?.messageRes)
                return@launch
            }
            showSnackbar(view)
            lastDeletedNote = note
        }
    }

    private fun showSnackbar(view: View) {
        Snackbar.make(view, R.string.note_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { undoDelete(view) }.show()
    }

    private fun undoDelete(view: View) {
        if (lastDeletedNote == null) return

        viewModelScope.launch {
            val response = noteRepo.create(lastDeletedNote!!)
            if (response.isFailure()) {
                view.context.showToast(response.exception?.messageRes)
                return@launch
            }
            lastDeletedNote = null
        }
    }

}
