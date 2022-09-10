package dev.sertan.android.paper.common.util

import org.junit.jupiter.api.Test

internal class OneTimeStateTest {

    @Test
    fun `data should be not null - for the first time`() {
        val state = OneTimeState(1)
        assert(state.data != null)
    }

    @Test
    fun `data should be null - after the first time`() {
        val state = OneTimeState(1)
        state.data
        assert(state.data == null)
    }
}
