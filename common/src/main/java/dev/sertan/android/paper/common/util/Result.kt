package dev.sertan.android.paper.common.util

sealed class Result<out T> private constructor() {
    open val data: T? = null
    open val exception: PaperException? = null

    object Idle : Result<Nothing>()
    object Loading : Result<Nothing>()
    data class Success<T>(override val data: T? = null) : Result<T>()
    data class Failure(override val exception: PaperException? = null) : Result<Nothing>()

    val isIdle get() = this is Idle
    val isLoading get() = this is Loading
    val isSuccess get() = this is Success
    val isFailure get() = this is Failure

    fun <O> map(block: (T?) -> O?): Result<O> {
        return when {
            isIdle -> Idle
            isLoading -> Loading
            isSuccess -> Success(data = block(data))
            isFailure -> Failure(exception)
            else -> Failure(exception = PaperException.IllegalStateException)
        }
    }
}
