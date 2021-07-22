package dev.sertan.android.paper.data.db

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.model.Paper
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DbServiceModule::class])
object TestDbServiceModule {

    // Return an instance of which DbService class you want to test.
    @Provides
    @Singleton
    fun providePaperDbService(): DbService<Paper> = FakePaperDbService()
}
