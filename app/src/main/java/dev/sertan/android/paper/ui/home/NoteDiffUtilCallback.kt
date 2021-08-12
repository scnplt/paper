package dev.sertan.android.paper.ui.home

import androidx.recyclerview.widget.DiffUtil
import dev.sertan.android.paper.data.model.Note

internal object NoteDiffUtilCallback : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}
