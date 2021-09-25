package dev.sertan.android.paper.ui.editnote

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditNoteViewModel @Inject constructor(private val noteRepo: NoteRepo) : ViewModel() {
    private var job: Job? = null
    val note = MutableLiveData<Note>()

    fun update(view: View) {
        job?.cancel()
        job = viewModelScope.launch {
            val response = note.value?.run {
                updateDate = System.currentTimeMillis()
                noteRepo.update(this)
            } ?: return@launch

            if (response.isSuccess()) view.findNavController().popBackStack()
            view.context.showToast(response.exception?.messageRes)
        }
    }

}
