package dev.sertan.android.paper.data.database

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import dev.sertan.android.paper.common.util.PaperException
import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.common.util.tryGetResult
import dev.sertan.android.paper.data.model.DatabaseNote
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

internal class FirestoreNoteDatabaseService(
    private val collectionReference: CollectionReference
) : NoteDatabaseService {

    override suspend fun create(databaseNote: DatabaseNote): Result<Boolean> {
        return tryGetResult {
            collectionReference.document(databaseNote.uid).set(databaseNote).isSuccessful
        }
    }

    override suspend fun update(databaseNote: DatabaseNote): Result<Boolean> {
        return tryGetResult {
            collectionReference.document(databaseNote.uid).set(databaseNote).isSuccessful
        }
    }

    override suspend fun delete(databaseNote: DatabaseNote): Result<Boolean> {
        return tryGetResult { collectionReference.document(databaseNote.uid).delete().isSuccessful }
    }

    override suspend fun getNote(noteUid: String): Result<DatabaseNote?> {
        return tryGetResult {
            val document = collectionReference.document(noteUid).get().await()
            document.toObject<DatabaseNote>()
        }
    }

    override suspend fun getNotes(userUid: String): Flow<Result<List<DatabaseNote>>> {
        return callbackFlow {
            val listenerRegistration =
                collectionReference.whereEqualTo(DatabaseNote::userUid.name, userUid)
                    .orderBy(DatabaseNote::updateDateMillis.name, Query.Direction.DESCENDING)
                    .addSnapshotListener { value, error ->
                        error?.let { close(it) }
                        val notes =
                            value?.documents?.mapNotNull { document -> document.toObject<DatabaseNote>() }
                                .orEmpty()
                        trySend(Result.Success(notes))
                    }
            awaitClose { listenerRegistration.remove() }
        }.catch { Result.Failure(PaperException.IllegalStateException) }
    }

    companion object {
        const val NOTE_COLLECTION_NAME = "notes"
    }
}
