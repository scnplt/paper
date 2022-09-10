package dev.sertan.android.paper.data.repository

import dev.sertan.android.paper.common.util.mapToResult
import dev.sertan.android.paper.common.util.tryGetResult
import dev.sertan.android.paper.data.datasource.user.UserDataSource
import dev.sertan.android.paper.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

internal class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {

    override fun getUserUidAsStream(): Flow<Result<String?>> {
        return userDataSource.getUserUidAsStream().mapToResult { it }
    }

    override suspend fun setUserUid(userUid: String): Result<Boolean> = tryGetResult {
        userDataSource.setUserUid(userUid)
    }
}
