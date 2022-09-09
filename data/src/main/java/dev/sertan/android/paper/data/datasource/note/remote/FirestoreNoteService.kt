package dev.sertan.android.paper.data.datasource.note.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import dev.sertan.android.paper.data.datasource.note.NoteDataSource
import dev.sertan.android.paper.data.model.NoteEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class FirestoreNoteService(
    private val collectionReference: CollectionReference
) : NoteDataSource {

    override fun getAllAsStream(userUid: String): Flow<List<NoteEntity>> = callbackFlow {
        val listenerRegistration = collectionReference
            .whereEqualTo(NoteEntity::userUid.name, userUid)
            .orderBy(NoteEntity::updateDateMillis.name, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                error?.let { close(it) }
                val notes = snapshot?.documents
                    ?.mapNotNull { document -> document.toObject<NoteEntity>() }
                    .orEmpty()
                trySend(notes)
            }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getNote(noteUid: String): NoteEntity? {
        val document = collectionReference.document(noteUid).get().await()
        return document.toObject<NoteEntity>()
    }

    override suspend fun insert(vararg notes: NoteEntity) {
        notes.forEach { note -> collectionReference.document(note.uid).set(note).await() }
    }

    override suspend fun delete(note: NoteEntity) {
        collectionReference.document(note.uid).delete()
    }

    override suspend fun update(note: NoteEntity) {
        collectionReference.document(note.uid).set(note)
    }

    companion object {
        const val NOTE_COLLECTION_INJECTION_NAME = "noteCollectionInject"
        const val NOTE_COLLECTION_REFERENCE_NAME = "notes"
    }
}
