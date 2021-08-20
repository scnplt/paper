package dev.sertan.android.paper.util

import com.google.common.truth.Truth
import org.junit.Test
import java.util.*

internal class UtilsTest {

    @Test
    fun getCurrentDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
            .let { if (it.length == 1) "0$it" else it }
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            .let { if (it.length == 1) "0$it" else it }

        val expected = "$day/$month/$year"
        val actual = Utils.getCurrentDate()
        Truth.assertThat(actual).isEqualTo(expected)
    }

}
