package dev.sertan.android.paper.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserUidAsStream(): Flow<Result<String?>>
    suspend fun setUserUid(userUid: String): Result<Boolean>
}
