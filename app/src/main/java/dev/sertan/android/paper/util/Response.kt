package dev.sertan.android.paper.util

internal sealed class Response<out T>(
    val value: T? = null,
    val exception: PaperException? = null
) {

    fun isIdle(): Boolean = this is Idle
    fun isLoading(): Boolean = this is Loading
    fun isFailure(): Boolean = this is Failure
    fun isSuccess(): Boolean = this is Success

    object Idle : Response<Nothing>()
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T?) : Response<T>(data)
    data class Failure(val e: PaperException) : Response<Nothing>(exception = e)

    companion object {
        fun idle(): Idle = Idle
        fun loading(): Loading = Loading
        fun failure(e: PaperException? = null): Failure = Failure(e ?: PaperException.Default)
        fun <T> success(data: T? = null): Success<T> = Success(data)
    }

}
