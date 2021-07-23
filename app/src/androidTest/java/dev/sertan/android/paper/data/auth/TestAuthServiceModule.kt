package dev.sertan.android.paper.data.auth

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AuthServiceModule::class])
object TestAuthServiceModule {

    // Return an instance of which AuthService class you want to test.
    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FakeAuthService()
}
