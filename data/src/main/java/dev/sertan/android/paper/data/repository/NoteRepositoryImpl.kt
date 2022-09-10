package dev.sertan.android.paper.data.repository

import dev.sertan.android.paper.common.util.mapToResult
import dev.sertan.android.paper.common.util.tryGetResult
import dev.sertan.android.paper.data.datasource.note.NoteDataSource
import dev.sertan.android.paper.data.mapper.toNoteDto
import dev.sertan.android.paper.data.mapper.toNoteDtoList
import dev.sertan.android.paper.data.mapper.toNoteEntity
import dev.sertan.android.paper.data.mapper.toNoteEntityList
import dev.sertan.android.paper.data.model.NoteEntity
import dev.sertan.android.paper.domain.model.NoteDto
import dev.sertan.android.paper.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

internal class NoteRepositoryImpl(private val noteDataSource: NoteDataSource) : NoteRepository {

    override fun getAllAsStream(userUid: String): Flow<Result<List<NoteDto>>> {
        return noteDataSource.getAllAsStream(userUid).mapToResult(List<NoteEntity>::toNoteDtoList)
    }

    override suspend fun getNote(noteUid: String): Result<NoteDto?> = tryGetResult {
        val noteEntity = noteDataSource.getNote(noteUid)
        noteEntity.toNoteDto()
    }

    override suspend fun create(vararg notes: NoteDto): Result<Boolean> = tryGetResult {
        noteDataSource.insert(notes = notes.toNoteEntityList().toTypedArray())
        true
    }

    override suspend fun delete(noteDto: NoteDto): Result<Boolean> = tryGetResult {
        noteDataSource.delete(note = noteDto.toNoteEntity())
        true
    }

    override suspend fun update(noteDto: NoteDto): Result<Boolean> = tryGetResult {
        noteDataSource.update(note = noteDto.toNoteEntity())
        true
    }
}
