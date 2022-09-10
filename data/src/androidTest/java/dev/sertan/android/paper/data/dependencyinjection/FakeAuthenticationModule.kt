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
internal object FakeAuthenticationModule {

    /*
     * If you are using [FirebaseEmulatorSuite](https://firebase.google.com/docs/emulator-suite),
     * return "FirebaseAuthService" instead of "FakeAuthService"
     */
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return FakeAuthService()
    }
}
