package dev.sertan.android.paper.data.database

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.data.model.DatabaseNote
import kotlinx.coroutines.flow.Flow

interface NoteDatabaseService {
    suspend fun create(databaseNote: DatabaseNote): Result<Boolean>
    suspend fun update(databaseNote: DatabaseNote): Result<Boolean>
    suspend fun delete(databaseNote: DatabaseNote): Result<Boolean>
    suspend fun getNote(noteUid: String): Result<DatabaseNote?>
    suspend fun getNotes(userUid: String): Flow<Result<List<DatabaseNote>>>
}
