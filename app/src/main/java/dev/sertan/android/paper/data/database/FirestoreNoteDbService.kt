package dev.sertan.android.paper.data.database

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

private const val COLLECTION = "notes"

internal class FirestoreNoteDbService(firestore: FirebaseFirestore) : NoteDbService {
    private val collection: CollectionReference by lazy { firestore.collection(COLLECTION) }

    override suspend fun create(data: Note): Response<Unit> = try {
        collection.document(data.uid).set(data).await()
        Response.success()
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun delete(data: Note): Response<Unit> = try {
        collection.document(data.uid).delete().await()
        Response.success()
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun update(data: Note): Response<Unit> = try {
        collection.document(data.uid).set(data).await()
        Response.success()
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override suspend fun getNote(uid: String): Response<Note?> = try {
        val document = collection.document(uid).get().await()
        Response.success(document.toObject<Note>())
    } catch (e: FirebaseException) {
        Response.failure(e)
    }

    override fun getNotes(userUid: String): Flow<Response<List<Note>?>> = callbackFlow {
        trySend(Response.loading())
        val listenerRegistration = collection.whereEqualTo(Note::userUid.name, userUid)
            .orderBy(Note::updateDate.name, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let { close(it) }
                val notes = value?.documents?.map { document -> document.toObject<Note>()!! }
                trySend(Response.success(notes))
            }
        awaitClose { listenerRegistration.remove() }
    }.catch { e -> Response.failure(e) }
}
