package dev.sertan.android.paper.util

internal sealed class Response<out T>(
    val value: T? = null,
    val exception: Throwable? = null
) {
    val isIdle get() = this is Idle
    val isLoading get() = this is Loading
    val isFailure get() = this is Failure
    val isSuccess get() = this is Success

    object Idle : Response<Nothing>()
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T?) : Response<T>(data)
    data class Failure(val e: Throwable?) : Response<Nothing>(exception = e)

    companion object {
        fun idle(): Idle = Idle
        fun loading(): Loading = Loading
        fun failure(e: Throwable? = null): Failure = Failure(e)
        fun <T> success(data: T? = null): Success<T> = Success(data)
    }

}
