package dev.sertan.android.paper.data.module

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.database.FirestoreNoteDbService
import dev.sertan.android.paper.data.database.NoteDbService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DbServiceModule {

    @Provides
    @Singleton
    fun provideNoteDbService(): NoteDbService = FirestoreNoteDbService(Firebase.firestore)

}
