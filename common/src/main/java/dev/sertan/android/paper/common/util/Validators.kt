package dev.sertan.android.paper.common.util

import androidx.core.util.PatternsCompat

private const val MIN_PASSWORD_LENGTH = 8

fun validateEmailAddress(address: String): Boolean {
    return PatternsCompat.EMAIL_ADDRESS.matcher(address).matches()
}

fun validatePassword(pwd: String): Boolean {
    return pwd.trim().length >= MIN_PASSWORD_LENGTH
}
