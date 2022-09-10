package dev.sertan.android.paper.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

inline fun <T> tryOrGetDefault(defaultValue: T, block: () -> T): T {
    return try {
        block()
    } catch (_: Exception) {
        defaultValue
    }
}

@Suppress("TooGenericExceptionCaught")
inline fun <T> tryGetResult(block: () -> T): Result<T> {
    return try {
        Result.success(value = block())
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}

inline fun <I, O> Flow<I>.mapToResult(crossinline block: (I) -> O): Flow<Result<O>> {
    return map { Result.success(block(it)) }
        .catch { exception -> emit(Result.failure(exception)) }
}
