package dev.sertan.android.paper.data.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.datasource.note.NoteDataSource
import dev.sertan.android.paper.data.datasource.note.local.NoteDatabase
import dev.sertan.android.paper.data.datasource.note.remote.FirestoreNoteService
import dev.sertan.android.paper.data.datasource.note.remote.FirestoreNoteService.Companion.NOTE_COLLECTION_INJECTION_NAME
import dev.sertan.android.paper.data.datasource.note.remote.FirestoreNoteService.Companion.NOTE_COLLECTION_REFERENCE_NAME
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataSourceModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase::class.java.name
        ).build()
    }

    @Provides
    @Singleton
    @Named(NOTE_COLLECTION_INJECTION_NAME)
    fun provideNoteCollectionReference(): CollectionReference {
        return Firebase.firestore.collection(NOTE_COLLECTION_REFERENCE_NAME)
    }

    @Provides
    @Singleton
    @Named(NoteDataSource.REMOTE_INJECTION_NAME)
    fun provideRemoteNoteDataSource(
        @Named(NOTE_COLLECTION_INJECTION_NAME) collectionReference: CollectionReference
    ): NoteDataSource {
        return FirestoreNoteService(collectionReference)
    }

    @Provides
    @Singleton
    @Named(NoteDataSource.LOCAL_INJECTION_NAME)
    fun provideLocalNoteDataSource(database: NoteDatabase): NoteDataSource {
        return database.noteDao()
    }
}
