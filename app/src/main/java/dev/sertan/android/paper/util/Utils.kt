package dev.sertan.android.paper.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(time)
    }

}
