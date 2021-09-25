package dev.sertan.android.paper.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val simpleDateFormat
            by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    @JvmStatic
    fun timestampToSimpleDate(value: Long): String {
        val date = Date(value)
        return simpleDateFormat.format(date)
    }

}
