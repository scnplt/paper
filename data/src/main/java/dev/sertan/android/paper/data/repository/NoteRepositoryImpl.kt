package dev.sertan.android.paper.data.repository

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.data.database.NoteDatabaseService
import dev.sertan.android.paper.data.mapper.toDatabaseNote
import dev.sertan.android.paper.data.mapper.toNoteDto
import dev.sertan.android.paper.data.mapper.toNoteDtoList
import dev.sertan.android.paper.domain.repository.NoteRepository
import dev.sertan.android.paper.domain.model.NoteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(private val noteDatabaseService: NoteDatabaseService) : NoteRepository {

    override suspend fun create(noteDto: NoteDto): Result<Boolean> {
        return noteDatabaseService.create(databaseNote = noteDto.toDatabaseNote())
    }

    override suspend fun update(noteDto: NoteDto): Result<Boolean> {
        return noteDatabaseService.update(databaseNote = noteDto.toDatabaseNote())
    }

    override suspend fun delete(noteDto: NoteDto): Result<Boolean> {
        return noteDatabaseService.delete(databaseNote = noteDto.toDatabaseNote())
    }

    override suspend fun getNote(noteUid: String): Result<NoteDto?> {
        return noteDatabaseService.getNote(noteUid).map { databaseNote -> databaseNote.toNoteDto() }
    }

    override suspend fun getNotes(userUid: String): Flow<Result<List<NoteDto>>> {
        return noteDatabaseService.getNotes(userUid).map { result ->
            result.map { databaseNoteList ->
                databaseNoteList?.toNoteDtoList()
            }
        }
    }
}
