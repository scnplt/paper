package dev.sertan.android.paper.common.util

import androidx.annotation.StringRes
import dev.sertan.android.paper.common.R

sealed class PaperException(@StringRes val messageRes: Int) {
    object InvalidEmailException : PaperException(R.string.invalid_email_address)
    object InvalidEmailOrPasswordException : PaperException(R.string.invalid_email_or_password)
    object IllegalStateException : PaperException(R.string.unexpected_situation_was_encountered)
}
