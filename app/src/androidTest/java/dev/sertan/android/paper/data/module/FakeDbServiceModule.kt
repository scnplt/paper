package dev.sertan.android.paper.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sertan.android.paper.data.db.DbService
import dev.sertan.android.paper.data.db.FakePaperDbService
import dev.sertan.android.paper.data.model.Paper
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DbServiceModule::class])
internal object FakeDbServiceModule {

    @Provides
    @Singleton
    fun providePaperDbService(): DbService<Paper> = FakePaperDbService()
}
