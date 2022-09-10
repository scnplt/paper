package dev.sertan.android.paper.domain.repository

import dev.sertan.android.paper.domain.model.NoteDto
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllAsStream(userUid: String): Flow<Result<List<NoteDto>>>
    suspend fun getNote(noteUid: String): Result<NoteDto?>
    suspend fun create(vararg notes: NoteDto): Result<Boolean>
    suspend fun delete(noteDto: NoteDto): Result<Boolean>
    suspend fun update(noteDto: NoteDto): Result<Boolean>

    companion object {
        const val CACHE_REPOSITORY_INJECTION_NAME = "cacheNoteRepositoryInject"
        const val REMOTE_REPOSITORY_INJECTION_NAME = "remoteNoteRepositoryInject"
    }
}
