package dev.sertan.android.paper.data.datasource.note

import dev.sertan.android.paper.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class FakeNoteDataSource : NoteDataSource {
    private val notes = mutableListOf<NoteEntity>()

    override suspend fun update(note: NoteEntity) {
        val noteIndex = notes.indexOfFirst { it.uid == note.uid }
        if (noteIndex == -1) return
        notes[noteIndex] = note
    }

    override suspend fun delete(note: NoteEntity) {
        notes.remove(note)
    }

    override fun getAllAsStream(userUid: String): Flow<List<NoteEntity>> {
        return flow { emit(notes.filter { it.userUid == userUid }) }
    }

    override suspend fun getNote(noteUid: String): NoteEntity? {
        return notes.firstOrNull { it.uid == noteUid }
    }

    override suspend fun insert(vararg notes: NoteEntity) {
        this.notes.addAll(notes)
    }
}
