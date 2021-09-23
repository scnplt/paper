package dev.sertan.android.paper.data.db

import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.PaperException
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class FakeNoteDbService : DbService<Note> {
    private val notes = mutableListOf<Note>()

    override suspend fun create(data: Note): Response<Unit> {
        notes.add(data)
        return Response.success()
    }

    override suspend fun delete(data: Note): Response<Unit> {
        if (!notes.contains(data)) return Response.failure(PaperException.DataNotFound)
        notes.remove(data)
        return Response.success()
    }

    override suspend fun update(data: Note): Response<Unit> {
        val index = notes.indexOfFirst { it.uid == data.uid }
        if (index == -1) return Response.failure(PaperException.DataNotFound)
        notes[index] = data
        return Response.success()
    }

    override suspend fun getData(uid: String): Response<Note> {
        val index = notes.indexOfFirst { it.uid == uid }
        if (index == -1) return Response.failure(PaperException.DataNotFound)
        return Response.success(notes[index])
    }

    override fun getAllData(userUid: String): Flow<Response<List<Note>>> {
        return flow {
            emit(Response.loading())
            val mPapers = notes.filter { it.userUid == userUid }
            if (mPapers.isEmpty()) throw PaperException.DataNotFound
            emit(Response.success(mPapers))
        }.catch { e ->
            val exception = if (e is PaperException) e else null
            emit(Response.failure(exception))
        }
    }

}
