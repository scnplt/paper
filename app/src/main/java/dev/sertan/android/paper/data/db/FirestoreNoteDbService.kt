package dev.sertan.android.paper.data.db

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.PaperException
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

internal class FirestoreNoteDbService : DbService<Note> {
    private val collection: CollectionReference by lazy { Firebase.firestore.collection(COLLECTION) }

    companion object {
        private const val COLLECTION = "notes"
    }

    override suspend fun create(data: Note): Response<Unit> {
        return try {
            collection.document(data.uid).set(data).await()
            Response.success()
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: FirebaseFirestoreException) {
            Response.failure()
        }
    }

    override suspend fun delete(data: Note): Response<Unit> {
        return try {
            if (getData(data.uid).isFailure()) throw PaperException.DataNotFound
            collection.document(data.uid).delete().await()
            Response.success()
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: FirebaseFirestoreException) {
            Response.failure()
        }
    }

    override suspend fun update(data: Note): Response<Unit> {
        return try {
            if (getData(data.uid).isFailure()) throw PaperException.DataNotFound
            collection.document(data.uid).set(data).await()
            Response.success()
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: FirebaseFirestoreException) {
            Response.failure()
        }
    }

    override suspend fun getData(uid: String): Response<Note> {
        return try {
            val document = collection.document(uid).get().await()
            val note = document.toObject<Note>() ?: throw PaperException.DataNotFound
            Response.success(note)
        } catch (e: PaperException) {
            Response.failure(e)
        } catch (e: FirebaseFirestoreException) {
            Response.failure()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllData(userUid: String): Flow<Response<List<Note>>> {
        return callbackFlow {
            trySend(Response.loading())

            val listenerRegistration =
                collection.whereEqualTo("userUid", userUid)
                    .orderBy("updateDate", Query.Direction.DESCENDING)
                    .addSnapshotListener { value, error ->
                        if (error != null) cancel()
                        val notes =
                            value?.documents?.map { document -> document.toObject<Note>()!! }
                        trySend(Response.success(notes))
                    }

            awaitClose { listenerRegistration.remove() }
        }.catch { emit(Response.failure()) }
    }

}
