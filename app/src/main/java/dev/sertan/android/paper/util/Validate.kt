package dev.sertan.android.paper.util

import androidx.core.util.PatternsCompat

object Validate {

    fun email(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun password(password: String): Boolean {
        return password.length >= 8
    }

}
