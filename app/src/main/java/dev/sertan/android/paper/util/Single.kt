package dev.sertan.android.paper.util

internal class Single<T> {
    private var isFirst = true

    var value: T?
        get() {
            if (isFirst) {
                isFirst = false
                return field.also { field = null }
            }
            return field
        }
        set(value) {
            isFirst = true
            field = value
        }

    constructor(value: T?) { this.value = value }
}
