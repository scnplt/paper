package dev.sertan.android.paper.util

import com.google.common.truth.Truth
import org.junit.Test

internal class ValidateTest {

    @Test
    fun `validate email successful`() {
        val email = "test@test.com"
        val result = Validate.email(email)
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `validate email failed`() {
        val email = "test"
        val result = Validate.email(email)
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `validate password successful`() {
        val password = "12345678"
        val result = Validate.password(password)
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `validate password failed`() {
        val password = ""
        val result = Validate.password(password)
        Truth.assertThat(result).isFalse()
    }
}
