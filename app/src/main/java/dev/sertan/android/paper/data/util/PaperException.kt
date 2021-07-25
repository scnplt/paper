package dev.sertan.android.paper.data.util

import android.content.Context
import androidx.annotation.StringRes
import dev.sertan.android.paper.R

sealed class PaperException(@StringRes private val uiMessageId: Int) : Exception() {
    object Default : PaperException(R.string.default_exception_message)
    object InvalidEmailAddress : PaperException(R.string.invalid_email_message)
    object InvalidPassword : PaperException(R.string.invalid_password_message)
    object UserAlreadyExists : PaperException(R.string.user_already_exists_message)
    object IncorrectInformation : PaperException(R.string.incorrect_information_message)
    object UserNotFound : PaperException(R.string.user_not_found_message)
    object DataNotFound : PaperException(R.string.data_not_found_message)

    fun getUIMessage(context: Context): String {
        return context.getString(uiMessageId)
    }
}
