package dev.sertan.android.paper.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.authentication.AuthService
import dev.sertan.android.paper.data.authentication.FakeAuthService
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AuthServiceModule::class])
internal object FakeAuthServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        /* To test FirebaseAuthService using Firebase Local Emulator Suite,
        create FirebaseAuth object by entering host and port values and
        return it with FirebaseAuthService. */

        return FakeAuthService()
    }

}
