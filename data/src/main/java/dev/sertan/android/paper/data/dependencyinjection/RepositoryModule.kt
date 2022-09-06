package dev.sertan.android.paper.data.dependencyinjection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.database.NoteDatabaseService
import dev.sertan.android.paper.data.repository.NoteRepositoryImpl
import dev.sertan.android.paper.data.repository.UserRepositoryImpl
import dev.sertan.android.paper.domain.repository.NoteRepository
import dev.sertan.android.paper.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(noteDatabaseService: NoteDatabaseService): NoteRepository {
        return NoteRepositoryImpl(noteDatabaseService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(authService: AuthService): UserRepository = UserRepositoryImpl(authService)
}
