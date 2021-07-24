package dev.sertan.android.paper.data.util

import androidx.core.util.PatternsCompat

object Validator {
    fun validateEmail(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length > 7
    }
}
