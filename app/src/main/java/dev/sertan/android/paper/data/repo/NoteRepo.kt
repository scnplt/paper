package dev.sertan.android.paper.data.repo

import dev.sertan.android.paper.data.db.DbService
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NoteRepo @Inject constructor(private val dbService: DbService<Note>) {

    suspend fun create(note: Note): Response<Unit> = dbService.create(note)

    suspend fun delete(note: Note): Response<Unit> = dbService.delete(note)

    suspend fun update(note: Note): Response<Unit> = dbService.update(note)

    suspend fun getData(uid: String): Response<Note> = dbService.getData(uid)

    fun getAllData(userUid: String): Flow<Response<List<Note>>> = dbService.getAllData(userUid)

}
