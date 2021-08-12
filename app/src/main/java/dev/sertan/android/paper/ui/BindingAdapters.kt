package dev.sertan.android.paper.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.ui.home.NoteAdapter
import dev.sertan.android.paper.util.Response

@BindingAdapter("notes")
internal fun bindNotesToRecyclerView(view: RecyclerView, notesResponse: Response<List<Note>>) {
    if (notesResponse.isFailure() || view.adapter !is NoteAdapter) return
    (view.adapter as NoteAdapter).submitList(notesResponse.value)
}
