package dev.sertan.android.paper.domain.model

@JvmInline
value class Date private constructor(val millisecond: Long) {

    init {
        require(millisecond >= 0)
    }

    val second: Long
        get() = millisecond / MILLISECOND_IN_SECOND

    companion object {
        private const val MILLISECOND_IN_SECOND = 1000

        fun fromMillisecond(millisecond: Long): Date = Date(millisecond)
    }
}
