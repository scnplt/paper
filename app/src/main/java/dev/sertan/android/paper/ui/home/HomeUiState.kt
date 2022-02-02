package dev.sertan.android.paper.ui.home

import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.Response
import dev.sertan.android.paper.util.Single

internal data class HomeUiState(
    val notes: Response<List<Note>?> = Response.idle(),
    val noteDeleted: Single<Boolean> = Single(false),
    val message: Single<String> = Single(null),
) {
    val isEmpty: Boolean get() = notes.value.isNullOrEmpty()
}
