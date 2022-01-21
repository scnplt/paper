package dev.sertan.android.paper.util

import com.google.common.truth.Truth
import org.junit.Test

internal class UtilsTest {

    @Test
    fun timestampToSimpleDate() {
        val timestamp = 1355314332000L
        val simpleDate = Utils.timestampToSimpleDate(timestamp)
        Truth.assertThat(simpleDate).isEqualTo("12/12/2012")
    }
}
