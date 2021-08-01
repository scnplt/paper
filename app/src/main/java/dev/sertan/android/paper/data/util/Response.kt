package dev.sertan.android.paper.data.util

internal sealed class Response<out T> private constructor(
    val value: T? = null,
    val exception: Exception? = null
) {
    companion object {
        fun idle(): Idle = Idle
        fun loading(): Loading = Loading
        fun failure(e: Exception = PaperException.Default): Failure = Failure(e)
        fun <T> success(data: T? = null): Success<T> = Success(data)
    }

    fun isIdle(): Boolean = this is Idle
    fun isLoading(): Boolean = this is Loading
    fun isFailure(): Boolean = this is Failure
    fun isSuccess(): Boolean = this is Success

    object Idle : Response<Nothing>()
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T?) : Response<T>(data)
    data class Failure(val e: Exception) : Response<Nothing>(exception = e)
}
