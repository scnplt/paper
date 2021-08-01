package dev.sertan.android.paper.data.repo

import dev.sertan.android.paper.data.db.DbService
import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.util.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
internal class PaperRepo @Inject constructor(private val dbService: DbService<Paper>) {

    suspend fun create(paper: Paper): Response<Unit> {
        return dbService.create(paper)
    }

    suspend fun delete(paper: Paper): Response<Unit> {
        return dbService.delete(paper)
    }

    suspend fun update(paper: Paper): Response<Unit> {
        return dbService.update(paper)
    }

    suspend fun getData(uid: String): Response<Paper> {
        return dbService.getData(uid)
    }

    fun getAllData(userUid: String): Flow<Response<List<Paper>>> {
        return dbService.getAllData(userUid)
    }

}
