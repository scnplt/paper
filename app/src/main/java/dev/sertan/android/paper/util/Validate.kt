package dev.sertan.android.paper.util

import androidx.core.util.PatternsCompat

object Validate {
    private const val MIN_PASSWORD_LENGTH = 8

    fun email(email: String): Boolean = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

    fun password(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH

}
