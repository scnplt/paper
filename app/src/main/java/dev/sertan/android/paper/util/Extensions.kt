package dev.sertan.android.paper.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes textRes: Int?) {
    Toast.makeText(this, textRes ?: return, Toast.LENGTH_SHORT).show()
}
