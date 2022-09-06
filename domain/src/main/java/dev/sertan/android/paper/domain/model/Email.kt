package dev.sertan.android.paper.domain.model

import dev.sertan.android.paper.common.util.validateEmailAddress

@JvmInline
value class Email private constructor(private val address: String) {

    init {
        require(validateEmailAddress(address = address))
    }

    override fun toString(): String = address

    companion object {
        fun create(emailAddress: String): Email = Email(address = emailAddress)
    }
}
