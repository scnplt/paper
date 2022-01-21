package dev.sertan.android.paper.ui.editnote

import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.Single

internal data class EditNoteUiState(
    val note: Note = Note(),
    val isUpdated: Boolean = false,
    val message: Single<String> = Single(null),
)
