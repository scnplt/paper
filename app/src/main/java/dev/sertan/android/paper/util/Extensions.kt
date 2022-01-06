package dev.sertan.android.paper.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String?) {
    Toast.makeText(this, message ?: return, Toast.LENGTH_SHORT).show()
}
