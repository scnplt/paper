package dev.sertan.android.paper.data.dependencyinjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.datasource.note.FakeNoteDataSource
import dev.sertan.android.paper.data.datasource.note.NoteDataSource
import dev.sertan.android.paper.data.datasource.note.local.NoteDatabase
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataSourceModule::class])
internal object FakeDataSourceModule {

    @Provides
    @Singleton
    fun provideFakeNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideFakeLocalDataSource(): NoteDataSource = FakeNoteDataSource()
}
