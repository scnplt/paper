package dev.sertan.android.paper.common.util

inline fun <T> tryOrGetDefault(defaultValue: T, block: () -> T): T {
    return try {
        block()
    } catch (_: Exception) {
        defaultValue
    }
}
