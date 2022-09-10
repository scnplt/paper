package dev.sertan.android.paper.data.dependencyinjection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.datasource.note.NoteDataSource
import dev.sertan.android.paper.data.datasource.user.UserDataSource
import dev.sertan.android.paper.data.repository.AuthRepositoryImpl
import dev.sertan.android.paper.data.repository.NoteRepositoryImpl
import dev.sertan.android.paper.data.repository.UserRepositoryImpl
import dev.sertan.android.paper.domain.repository.AuthRepository
import dev.sertan.android.paper.domain.repository.NoteRepository
import dev.sertan.android.paper.domain.repository.UserRepository
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    @Named(NoteRepository.CACHE_REPOSITORY_INJECTION_NAME)
    fun provideCacheNoteRepository(
        @Named(NoteDataSource.LOCAL_INJECTION_NAME) dataSource: NoteDataSource
    ): NoteRepository {
        return NoteRepositoryImpl(noteDataSource = dataSource)
    }

    @Provides
    @Singleton
    @Named(NoteRepository.REMOTE_REPOSITORY_INJECTION_NAME)
    fun provideRemoteNoteRepository(
        @Named(NoteDataSource.REMOTE_INJECTION_NAME) dataSource: NoteDataSource
    ): NoteRepository {
        return NoteRepositoryImpl(noteDataSource = dataSource)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository {
        return AuthRepositoryImpl(authService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }
}
