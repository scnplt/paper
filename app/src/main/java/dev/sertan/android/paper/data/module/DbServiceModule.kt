package dev.sertan.android.paper.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.db.DbService
import dev.sertan.android.paper.data.db.FirestorePaperDbService
import dev.sertan.android.paper.data.model.Paper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DbServiceModule {

    @Provides
    @Singleton
    fun providePaperDbService(): DbService<Paper> = FirestorePaperDbService()
}
