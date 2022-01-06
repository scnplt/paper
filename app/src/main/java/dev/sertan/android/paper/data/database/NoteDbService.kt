package dev.sertan.android.paper.data.database

import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.flow.Flow

internal interface NoteDbService {
    suspend fun create(data: Note): Response<Unit>
    suspend fun delete(data: Note): Response<Unit>
    suspend fun update(data: Note): Response<Unit>
    suspend fun getNote(uid: String): Response<Note?>
    fun getNotes(userUid: String): Flow<Response<List<Note>?>>
}
