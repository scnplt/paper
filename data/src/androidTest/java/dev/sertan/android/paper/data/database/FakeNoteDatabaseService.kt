package dev.sertan.android.paper.data.database

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.common.util.tryGetResult
import dev.sertan.android.paper.data.model.DatabaseNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class FakeNoteDatabaseService : NoteDatabaseService {

    private val notes = mutableListOf<DatabaseNote>()

    override suspend fun create(databaseNote: DatabaseNote): Result<Boolean> {
        return tryGetResult { notes.add(databaseNote) }
    }

    override suspend fun update(databaseNote: DatabaseNote): Result<Boolean> {
        return tryGetResult {
            val noteIndex = notes.indexOfFirst { it.uid == databaseNote.uid }
            if (noteIndex == -1) error("Not found!")
            notes[noteIndex] = databaseNote
            true
        }
    }

    override suspend fun delete(databaseNote: DatabaseNote): Result<Boolean> {
        return tryGetResult {
            if (notes.indexOfFirst { it.uid == databaseNote.uid } == -1) error("Not found")
            notes.remove(databaseNote) }
    }

    override suspend fun getNote(noteUid: String): Result<DatabaseNote?> {
        return tryGetResult { notes.firstOrNull { it.uid == noteUid } }
    }

    override suspend fun getNotes(userUid: String): Flow<Result<List<DatabaseNote>>> {
        return flow { emit(Result.Success(data = notes.filter { it.userUid == userUid })) }
    }
}
