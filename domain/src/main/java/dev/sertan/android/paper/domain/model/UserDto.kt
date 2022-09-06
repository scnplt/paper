package dev.sertan.android.paper.domain.model

data class UserDto(
    val uid: String,
    val email: Email,
    val password: Password?
)
