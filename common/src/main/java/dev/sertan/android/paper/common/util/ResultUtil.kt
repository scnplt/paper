package dev.sertan.android.paper.common.util

@Suppress("TooGenericExceptionCaught")
suspend fun <T> tryGetResult(callback: suspend () -> T): Result<T> {
    return try {
        Result.Success(data = callback())
    } catch (_: Exception) {
        Result.Failure(PaperException.IllegalStateException)
    }
}
