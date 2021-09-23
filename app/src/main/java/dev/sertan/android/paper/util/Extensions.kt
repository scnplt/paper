package dev.sertan.android.paper.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(message: String?) {
    if (message == null) return
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes textRes: Int?) {
    if (textRes == null) return
    Toast.makeText(this, textRes, Toast.LENGTH_SHORT).show()
}
