package dev.sertan.android.paper.data.dependencyinjection

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.repository.UserRepositoryImpl
import dev.sertan.android.paper.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
internal object FakeRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(authService: AuthService): UserRepository {
        return UserRepositoryImpl(authService)
    }
}
