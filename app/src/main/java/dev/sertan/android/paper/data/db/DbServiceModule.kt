package dev.sertan.android.paper.data.db

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.paper.data.model.Paper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbServiceModule {

    @Provides
    @Singleton
    fun providePaperDbService(): DbService<Paper> = FirestorePaperDbService()
}
