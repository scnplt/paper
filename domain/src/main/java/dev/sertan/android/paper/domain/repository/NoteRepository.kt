package dev.sertan.android.paper.domain.repository

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.domain.model.NoteDto
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun create(noteDto: NoteDto): Result<Boolean>
    suspend fun update(noteDto: NoteDto): Result<Boolean>
    suspend fun delete(noteDto: NoteDto): Result<Boolean>
    suspend fun getNote(noteUid: String): Result<NoteDto?>
    suspend fun getNotes(userUid: String): Flow<Result<List<NoteDto>>>
}
