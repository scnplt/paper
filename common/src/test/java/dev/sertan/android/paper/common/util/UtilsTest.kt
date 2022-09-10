package dev.sertan.android.paper.common.util

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

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

    @Suppress("TooGenericExceptionThrown")
    @Test
    fun `tryGetResult should return failure with Result - block has an exception`() {
        val result = tryGetResult { throw Exception() }
        assert(result.isFailure)
    }

    @Test
    fun `tryGetResult should return success with Result - block has not an exception`() {
        val expected = -1

        val result = tryGetResult { expected }
        assert(result.isSuccess)
        assert(result.getOrNull() == expected)
    }

    @Suppress("TooGenericExceptionThrown")
    @Test
    fun `mapToResult should return failure flow with Result - flow has an exception`() {
        val testFlow = flowOf(-1)

        val result = testFlow.mapToResult { throw Exception() }
        runBlocking { assert(result.firstOrNull()?.isFailure == true) }
    }

    @Test
    fun `mapToResult should return success flow with Result - flow has not an exception`() {
        val testFlow = flowOf(1)
        val expected = 2

        val result = testFlow.mapToResult { expected }
        runBlocking {
            assert(result.firstOrNull()?.isSuccess == true)
            assert(result.firstOrNull()?.getOrNull() == expected)
        }
    }
}
