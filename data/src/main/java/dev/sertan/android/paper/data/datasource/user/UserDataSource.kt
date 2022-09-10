package dev.sertan.android.paper.data.datasource.user

import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getUserUidAsStream(): Flow<String?>
    suspend fun setUserUid(userUid: String): Boolean
}
