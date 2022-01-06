package dev.sertan.android.paper.data.module

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.database.FakeNoteDbService
import dev.sertan.android.paper.data.database.FirestoreNoteDbService
import dev.sertan.android.paper.data.database.NoteDbService
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DbServiceModule::class])
internal object FakeDbServiceModule {

    @Provides
    @Singleton
    fun provideNoteDbService(): NoteDbService {
        /* To test FirestoreNoteDbService using Firebase Local Emulator Suite,
        create FirebaseFirestore object by entering host and port values and
        return it with FirestoreNoteDbService. */

        return FakeNoteDbService()
    }

}
