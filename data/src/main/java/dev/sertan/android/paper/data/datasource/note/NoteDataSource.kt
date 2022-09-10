package dev.sertan.android.paper.data.datasource.note

import dev.sertan.android.paper.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    fun getAllAsStream(userUid: String): Flow<List<NoteEntity>>
    suspend fun getNote(noteUid: String): NoteEntity?
    suspend fun insert(vararg notes: NoteEntity)
    suspend fun delete(note: NoteEntity)
    suspend fun update(note: NoteEntity)

    companion object {
        const val REMOTE_INJECTION_NAME = "remoteNoteDataSourceInject"
        const val LOCAL_INJECTION_NAME = "localNoteDataSourceInject"
    }
}
