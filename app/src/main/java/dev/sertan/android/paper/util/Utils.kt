package dev.sertan.android.paper.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    private val simpleDateFormat
            by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    @JvmStatic
    fun timestampToSimpleDate(value: Long): String = simpleDateFormat.format(Date(value))

}
