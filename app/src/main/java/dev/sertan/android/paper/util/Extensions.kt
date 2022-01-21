package dev.sertan.android.paper.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message ?: return, duration).show()
}
