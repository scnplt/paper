package dev.sertan.android.paper.data.module

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.authentication.FirebaseAuthService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AuthServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FirebaseAuthService(Firebase.auth)

}
