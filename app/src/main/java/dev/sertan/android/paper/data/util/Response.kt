package dev.sertan.android.paper.data.util

sealed class Response<out T> private constructor(
    val value: T? = null,
    val exception: Exception? = null
) {
    companion object {
        fun idle(): Idle = Idle
        fun loading(): Loading = Loading
        fun error(e: Exception = PaperException.Default): Error = Error(e)
        fun <T> success(data: T? = null): Success<T> = Success(data)
    }

    fun isIdle(): Boolean = this is Idle
    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    object Idle : Response<Nothing>()
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T?) : Response<T>(data)
    data class Error(val e: Exception) : Response<Nothing>(exception = e)
}
