package dev.sertan.android.paper.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.databinding.ItemNoteBinding

internal class NoteAdapter(private val listener: Listener) :
    ListAdapter<Note, NoteViewHolder>(NoteDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemNoteBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_note, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    fun interface Listener {
        fun onNoteClicked(note: Note)
    }

}
