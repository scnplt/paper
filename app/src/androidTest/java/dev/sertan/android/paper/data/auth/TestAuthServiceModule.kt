package dev.sertan.android.paper.data.auth

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAuthServiceModule {

    // Return an instance of which AuthService class you want to test.
    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FakeAuthService()
}
