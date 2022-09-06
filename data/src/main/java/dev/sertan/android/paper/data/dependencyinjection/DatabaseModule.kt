package dev.sertan.android.paper.data.dependencyinjection

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.database.FirestoreNoteDatabaseService
import dev.sertan.android.paper.data.database.FirestoreNoteDatabaseService.Companion.NOTE_COLLECTION_NAME
import dev.sertan.android.paper.data.database.NoteDatabaseService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabaseService(): NoteDatabaseService {
        val collectionReference = Firebase.firestore.collection(NOTE_COLLECTION_NAME)
        return FirestoreNoteDatabaseService(collectionReference = collectionReference)
    }
}
