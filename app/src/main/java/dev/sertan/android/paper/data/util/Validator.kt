package dev.sertan.android.paper.data.util

import android.util.Patterns

object Validator {
    fun validateEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun validatePassword(password: String): Boolean = password.length > 7
}
