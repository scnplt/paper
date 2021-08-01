package dev.sertan.android.paper.data.db

import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.data.util.PaperException
import dev.sertan.android.paper.data.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class FakePaperDbService : DbService<Paper> {
    private val papers = mutableListOf<Paper>()

    override suspend fun create(data: Paper): Response<Unit> {
        papers.add(data)
        return Response.success()
    }

    override suspend fun delete(data: Paper): Response<Unit> {
        if (!papers.contains(data)) return Response.failure(PaperException.DataNotFound)
        papers.remove(data)
        return Response.success()
    }

    override suspend fun update(data: Paper): Response<Unit> {
        val index = papers.indexOfFirst { it.uid == data.uid }
        if (index == -1) return Response.failure(PaperException.DataNotFound)
        papers[index] = data
        return Response.success()
    }

    override suspend fun getData(uid: String): Response<Paper> {
        val index = papers.indexOfFirst { it.uid == uid }
        if (index == -1) return Response.failure(PaperException.DataNotFound)
        return Response.success(papers[index])
    }

    override fun getAllData(userUid: String): Flow<Response<List<Paper>>> {
        return flow {
            emit(Response.loading())
            val mPapers = papers.filter { it.userUid == userUid }
            if (mPapers.isEmpty()) throw Exception()
            emit(Response.success(mPapers))
        }.catch { emit(Response.failure(PaperException.DataNotFound)) }
    }

}
