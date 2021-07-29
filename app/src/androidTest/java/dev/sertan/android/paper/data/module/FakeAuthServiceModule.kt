package dev.sertan.android.paper.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.auth.AuthService
import dev.sertan.android.paper.data.auth.FakeAuthService
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AuthServiceModule::class])
internal object FakeAuthServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FakeAuthService()
}
