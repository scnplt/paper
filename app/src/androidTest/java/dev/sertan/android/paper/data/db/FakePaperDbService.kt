package dev.sertan.android.paper.data.db

import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.data.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FakePaperDbService : DbService<Paper> {
    private val papers = mutableListOf<Paper>()

    override suspend fun create(data: Paper): Response<Paper> {
        return try {
            papers.add(data)
            Response.success(data)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun delete(data: Paper): Response<Boolean> {
        return try {
            val result = papers.remove(data)
            if (!result) throw DbException.DataNotFound
            Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun update(data: Paper): Response<Boolean> {
        return try {
            val index = papers.indexOfFirst { it.uid == data.uid }
            if (index == -1) throw DbException.DataNotFound
            papers[index] = data
            Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun getData(uid: String): Response<Paper> {
        return try {
            val index = papers.indexOfFirst { it.uid == uid }
            if (index == -1) throw DbException.DataNotFound
            Response.success(papers[index])
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override fun getAllData(userUid: String): Flow<Response<List<Paper>>> {
        return flow<Response<List<Paper>>> {
            val data = papers.filter { it.userUid == userUid }
            if (data.isEmpty()) throw DbException.DataNotFound
            emit(Response.success(data))
        }.catch {
            emit(Response.error(DbException.DataNotFound))
        }
    }

}
