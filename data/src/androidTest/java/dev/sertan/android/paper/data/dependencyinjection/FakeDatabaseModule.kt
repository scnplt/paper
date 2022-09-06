package dev.sertan.android.paper.data.dependencyinjection

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.database.FakeNoteDatabaseService
import dev.sertan.android.paper.data.database.NoteDatabaseService
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
internal object FakeDatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabaseService(): NoteDatabaseService = FakeNoteDatabaseService()
}
