package dev.sertan.android.paper.data.db

import dev.sertan.android.paper.data.util.Response
import kotlinx.coroutines.flow.Flow

internal interface DbService<T> {
    suspend fun create(data: T): Response<Unit>
    suspend fun delete(data: T): Response<Unit>
    suspend fun update(data: T): Response<Unit>
    suspend fun getData(uid: String): Response<T>
    fun getAllData(userUid: String): Flow<Response<List<T>>>
}
