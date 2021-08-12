package dev.sertan.android.paper.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String?) {
    if (message == null) return
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
