package dev.sertan.android.paper.ui.common

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.ui.home.NoteAdapter
import dev.sertan.android.paper.util.Response
import dev.sertan.android.paper.util.showToast

@BindingAdapter("notes")
internal fun bindNotesToRecyclerView(view: RecyclerView, notesResponse: Response<List<Note>>) {
    if (notesResponse.isFailure) {
        view.context.showToast(notesResponse.exception?.localizedMessage)
        return
    }
    (view.adapter as? NoteAdapter)?.submitList(notesResponse.value)
}
