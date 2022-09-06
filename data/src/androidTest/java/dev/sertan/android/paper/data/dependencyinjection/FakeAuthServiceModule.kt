package dev.sertan.android.paper.data.dependencyinjection

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.authentication.FakeAuthService
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AuthenticationModule::class])
internal object FakeAuthServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FakeAuthService()
}
