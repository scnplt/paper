package dev.sertan.android.paper.ui.addnote

import dev.sertan.android.paper.util.Single

internal data class AddNoteUiState(
    var title: String = "",
    var content: String = "",
    val isSaved: Boolean = false,
    val message: Single<String> = Single(null),
)
