package dev.sertan.android.paper.data.repo

import dev.sertan.android.paper.data.db.DbService
import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.data.util.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Singleton
class PaperRepo @Inject constructor(private val dbService: DbService<Paper>) {

    fun create(paper: Paper): Flow<Response<Paper>> {
        return flow {
            emit(Response.loading())
            val response = dbService.create(paper)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun delete(paper: Paper): Flow<Response<Unit>> {
        return flow {
            emit(Response.loading())
            val response = dbService.delete(paper)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun update(paper: Paper): Flow<Response<Unit>> {
        return flow {
            emit(Response.loading())
            val response = dbService.update(paper)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun getData(uid: String): Flow<Response<Paper>> {
        return flow {
            emit(Response.loading())
            val response = dbService.getData(uid)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun getAllData(userUid: String): Flow<Response<List<Paper>>> {
        return dbService.getAllData(userUid)
    }

}
