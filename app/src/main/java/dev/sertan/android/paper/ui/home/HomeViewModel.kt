package dev.sertan.android.paper.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val notes: LiveData<Response<List<Note>>> =
        Transformations.switchMap(userRepo.currentUser.asLiveData()) {
            it.value?.run { noteRepo.getAllData(this.uid).asLiveData() }
        }

    val isEmpty: LiveData<Boolean> = Transformations
        .map(notes) { it.value?.isNullOrEmpty() }

    fun deleteWithPosition(view: View, position: Int) {
        notes.value?.run {
            if (isFailure()) return
            val note = value?.get(position) ?: return

            viewModelScope.launch {
                val response = noteRepo.delete(note)
                if (response.isFailure()) {
                    view.context.run { showToast(response.exception?.getUIMessage(this)) }
                }
            }
        }
    }

}
