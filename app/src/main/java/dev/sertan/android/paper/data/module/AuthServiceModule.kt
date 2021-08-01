package dev.sertan.android.paper.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.auth.AuthService
import dev.sertan.android.paper.data.auth.FirebaseAuthService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AuthServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FirebaseAuthService()
}
