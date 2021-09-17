package dev.sertan.android.paper.ui.home

import androidx.recyclerview.widget.RecyclerView
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.databinding.ItemNoteBinding

internal class NoteViewHolder(private val binding: ItemNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note, listener: NoteAdapter.NoteListener) {
        binding.note = note
        binding.listener = listener
        binding.executePendingBindings()
    }

}
