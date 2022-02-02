package dev.sertan.android.paper.data.database

import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class FakeNoteDbService : NoteDbService {
    private val notes = mutableListOf<Note>()

    override suspend fun create(data: Note): Response<Unit> {
        notes.add(data)
        return Response.success()
    }

    override suspend fun delete(data: Note): Response<Unit> {
        if (!notes.contains(data)) return Response.failure()
        notes.remove(data)
        return Response.success()
    }

    override suspend fun update(data: Note): Response<Unit> {
        val index = notes.indexOfFirst { it.uid == data.uid }
        if (index == -1) return Response.failure()
        notes[index] = data
        return Response.success()
    }

    override suspend fun getNote(uid: String): Response<Note> {
        val index = notes.indexOfFirst { it.uid == uid }
        return Response.success(if (index == -1) null else notes[index])
    }

    override fun getNotes(userUid: String): Flow<Response<List<Note>>> {
        return flow {
            emit(Response.loading())
            val mPapers = notes.filter { it.userUid == userUid }
            emit(Response.success(mPapers))
        }.catch { e -> emit(Response.failure(e)) }
    }
}
