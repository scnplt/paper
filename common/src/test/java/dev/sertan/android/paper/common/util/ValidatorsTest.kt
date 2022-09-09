package dev.sertan.android.paper.common.util

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class ValidatorsTest {

    @TestFactory
    fun `email validation`() = listOf(
        "" to false,
        "a" to false,
        "asdasd@" to false,
        "   @asdas" to false,
        " @  asdas .com" to false,
        "asd@asd." to false,
        "asd@asd.com" to true,
        "asd @ asd . com" to false,
        "asd @asd.com" to false
    ).map { (email, expected) ->
        dynamicTest("Email \"$email\" is ${if (expected) "valid" else "invalid"}") {
            val actual = validateEmailAddress(address = email)
            assert(actual == expected)
        }
    }


    @TestFactory
    fun `password validation`() = listOf(
        "" to false,
        "a" to false,
        "asdasd" to false,
        "asdasdas" to true,
        "asdasdasdasd" to true,
        "213123asdasdasd" to true,
        "         " to false
    ).map { (password, expected) ->
        dynamicTest("Password \"$password\" is ${if (expected) "valid" else "invalid"}") {
            val actual = validatePassword(pwd = password)
            assert(actual == expected)
        }
    }
}
