package dev.sertan.android.paper.ui.note

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.data.repo.NoteRepo
import dev.sertan.android.paper.data.repo.UserRepo
import dev.sertan.android.paper.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NoteViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val noteRepo: NoteRepo
) : ViewModel() {
    private val scope by lazy { CoroutineScope(Dispatchers.IO) }
    private val userUid by lazy { userRepo.currentUser.value.value?.uid }

    val note = MutableStateFlow(Note())
    val createDate: String get() = Utils.timestampToSimpleDate(note.value.createDate)
    val updateDate: String get() = Utils.timestampToSimpleDate(note.value.updateDate)

    var screenMode: ScreenMode = ScreenMode.GET
    val isEditable get() = screenMode != ScreenMode.GET

    fun update() {
        val note = note.value.copy(updateDate = System.currentTimeMillis())
        scope.launch { noteRepo.update(note) }
    }

    fun create() {
        val note = note.value
        scope.launch {
            if (note.title.isNotBlank() || note.content.isNotBlank()) {
                note.copy(
                    title = note.title.trim(),
                    content = note.content.trim(),
                    userUid = userUid ?: return@launch
                ).let { noteRepo.create(it) }
            }
        }
    }

}
