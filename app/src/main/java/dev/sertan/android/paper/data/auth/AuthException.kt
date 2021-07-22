package dev.sertan.android.paper.data.auth

sealed class AuthException : Exception() {
    object InvalidEmail : AuthException()
    object InvalidPassword : AuthException()
    object UserAlreadyExist : AuthException()
    object IncorrectInformation : AuthException()
    object UserNotFound : AuthException()
}
