package dev.sertan.android.paper.common.util

import org.junit.Test

internal class UtilsTest {

    @Suppress("TooGenericExceptionThrown")
    @Test
    fun `tryOrGetDefault should return the default value - block has an exception`() {
        val defaultValue = -1

        val result = tryOrGetDefault(defaultValue) { throw Exception() }
        assert(result == defaultValue)
    }

    @Test
    fun `tryOrGetDefault should return a block return value - block has not an exception`() {
        val defaultValue = -1
        val expected = 5

        val result = tryOrGetDefault(defaultValue) { expected }
        assert(result == expected)
    }
}
