package dev.sertan.android.paper.data.dependencyinjection

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.datasource.note.FakeNoteDataSource
import dev.sertan.android.paper.data.datasource.note.NoteDataSource
import dev.sertan.android.paper.data.datasource.note.local.NoteDatabase
import dev.sertan.android.paper.data.datasource.user.CacheUserDataSource
import dev.sertan.android.paper.data.datasource.user.UserDataSource
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataSourceModule::class])
internal object FakeDataSourceModule {

    @Provides
    @Singleton
    fun provideFakeSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(this::class.simpleName, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFakeNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideFakeLocalDataSource(): NoteDataSource = FakeNoteDataSource()

    @Provides
    @Singleton
    fun provideFakeCacheUserDataSource(sharedPreferences: SharedPreferences): UserDataSource {
        return CacheUserDataSource(sharedPref = sharedPreferences)
    }
}
