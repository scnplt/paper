package dev.sertan.android.paper.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.db.DbService
import dev.sertan.android.paper.data.db.FakeNoteDbService
import dev.sertan.android.paper.data.model.Note
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DbServiceModule::class])
internal object FakeDbServiceModule {

    @Provides
    @Singleton
    fun provideNoteDbService(): DbService<Note> {
        /* To test FirestoreNoteDbService using Firebase Local Emulator Suite,
        create FirebaseFirestore object by entering host and port values and
        return it with FirestoreNoteDbService. */

        /*val firestore =
            FirebaseFirestore.getInstance().apply { useEmulator("10.0.2.2", 8080) }
        return FirestoreNoteDbService(firestore)*/

        return FakeNoteDbService()
    }

}
