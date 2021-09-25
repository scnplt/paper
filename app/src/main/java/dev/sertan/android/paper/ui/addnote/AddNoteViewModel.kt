package dev.sertan.android.paper.ui.addnote

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddNoteViewModel @Inject constructor(
    userRepo: UserRepo,
    private val noteRepo: NoteRepo
) : ViewModel() {

    private var job: Job? = null
    private val userUid: String? = userRepo.currentUser.value.run { value?.uid }

    val title = MutableLiveData("")
    val content = MutableLiveData("")

    fun save(view: View) {
        val title = title.value!!.trim()
        val content = content.value!!.trim()
        if (title.isBlank() && content.isBlank()) return

        job?.cancel()
        job = viewModelScope.launch {
            val note = Note(title, content, userUid ?: return@launch)
            val response = noteRepo.create(note)

            if (response.isSuccess()) view.findNavController().popBackStack()
            view.context.showToast(response.exception?.messageRes)
        }
    }

}
