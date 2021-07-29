package dev.sertan.android.paper.data.db

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.data.util.PaperException
import dev.sertan.android.paper.data.util.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class FirestorePaperDbService : DbService<Paper> {
    private val collection: CollectionReference by lazy { Firebase.firestore.collection(COLLECTION) }

    companion object {
        private const val COLLECTION = "papers"
    }

    override suspend fun create(data: Paper): Response<Paper> {
        return try {
            collection.document(data.uid).set(data).await()
            Response.success(data)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun delete(data: Paper): Response<Unit> {
        return try {
            if (getData(data.uid).isError()) throw PaperException.DataNotFound
            collection.document(data.uid).delete().await()
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun update(data: Paper): Response<Unit> {
        return try {
            if (getData(data.uid).isError()) throw PaperException.DataNotFound
            collection.document(data.uid).set(data).await()
            Response.success()
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun getData(uid: String): Response<Paper> {
        return try {
            val document = collection.document(uid).get().await()
            val paper = document.toObject<Paper>() ?: throw PaperException.DataNotFound
            Response.success(paper)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getAllData(userUid: String): Flow<Response<List<Paper>>> {
        return callbackFlow {
            trySend(Response.loading())

            val listenerRegistration = collection
                .whereEqualTo("userUid", userUid)
                .addSnapshotListener { value, error ->
                    if (error != null) cancel()
                    val papers = value?.documents?.map { document -> document.toObject<Paper>()!! }
                    if (papers.isNullOrEmpty()) cancel()
                    trySend(Response.success(papers))
                }

            awaitClose { listenerRegistration.remove() }
        }.catch {
            emit(Response.error(PaperException.DataNotFound))
        }.flowOn(Dispatchers.IO)
    }

}
