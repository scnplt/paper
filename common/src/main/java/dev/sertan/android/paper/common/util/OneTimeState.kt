package dev.sertan.android.paper.common.util

class OneTimeState<T>(data: T? = null) {
    var data: T? = data
        get() = field?.also { field = null }
}
