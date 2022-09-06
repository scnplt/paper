package dev.sertan.android.paper.domain.model

import dev.sertan.android.paper.common.util.validatePassword

@JvmInline
value class Password private constructor(private val password: String) {

    init {
        require(validatePassword(pwd = password))
    }

    override fun toString(): String = password

    companion object {
        fun create(pwd: String): Password = Password(password = pwd)
    }
}
