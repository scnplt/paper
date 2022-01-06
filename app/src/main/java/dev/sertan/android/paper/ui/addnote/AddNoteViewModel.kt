package dev.sertan.android.paper.ui.addnote

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.showToast
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class AddNoteViewModel @Inject constructor(
    userRepo: UserRepo,
    private val noteRepo: NoteRepo
) : ViewModel() {

    private var job: Job? = null
    private val userUid: String? = userRepo.currentUser.value.run { value?.uid }

    val title = MutableStateFlow("")
    val content = MutableStateFlow("")

    fun save(view: View) {
        job?.cancel()
        job = viewModelScope.launch {
            val note = Note(
                title = title.value.trim(),
                content = content.value.trim(),
                userUid = userUid ?: return@launch
            )
            val response = noteRepo.create(note)
            if (response.isSuccess) view.findNavController().popBackStack()
            view.context.showToast(response.exception?.localizedMessage)
        }
    }

}
