package dev.sertan.android.paper.data.auth

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Test

@SmallTest
internal class AuthServiceTest {
    private val service: AuthService = FakeAuthService()

    private val email = "test@test.com"
    private val password = "12345678"

    @Test
    fun currentUser() {
        runBlocking {
            // Return Response.Success
            service.register(email, password)
            service.logIn(email, password)
            val successResponse = service.currentUser()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value?.email).isEqualTo(email)

            // Return Response.Error with UserNotFound exception
            val errorResponse = service.currentUser()
            Truth.assertThat(errorResponse.isError()).isTrue()
            Truth.assertThat(errorResponse.exception is AuthException.UserNotFound).isTrue()
        }
    }

    @Test
    fun register() {
        runBlocking {
            // Return Response.Success
            val successResponse = service.register(email, password)
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with UserAlreadyExist exception
            service.register(email, password)
            val userAlreadyExist = service.register(email, password)
            clear()
            Truth.assertThat(userAlreadyExist.isError()).isTrue()
            Truth.assertThat(userAlreadyExist.exception is AuthException.UserAlreadyExist).isTrue()

            // Return Response.Error with InvalidEmail exception
            val invalidEmail = service.register("", password)
            Truth.assertThat(invalidEmail.isError()).isTrue()
            Truth.assertThat(invalidEmail.exception is AuthException.InvalidEmail).isTrue()

            // Return Response.Error with InvalidPassword exception
            val invalidPassword = service.register(email, "")
            Truth.assertThat(invalidPassword.isError()).isTrue()
            Truth.assertThat(invalidPassword.exception is AuthException.InvalidPassword).isTrue()
        }
    }

    @Test
    fun logIn() {
        runBlocking {
            // Return Response.Success
            service.register(email, password)
            val successResponse = service.logIn(email, password)
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with IncorrectInformation exception
            service.register(email, password)
            val incorrectInformation1 = service.logIn(email, "00000000")
            clear()
            Truth.assertThat(incorrectInformation1.isError()).isTrue()
            Truth.assertThat(incorrectInformation1.exception is AuthException.IncorrectInformation)
                .isTrue()

            // Return Response.Error with IncorrectInformation exception
            service.register(email, password)
            val incorrectInformation2 = service.logIn("aaaa@test.com", password)
            clear()
            Truth.assertThat(incorrectInformation2.isError()).isTrue()
            Truth.assertThat(incorrectInformation2.exception is AuthException.IncorrectInformation)
                .isTrue()

            // Return Response.Error with InvalidEmail exception
            val invalidEmail = service.logIn("", password)
            Truth.assertThat(invalidEmail.isError()).isTrue()
            Truth.assertThat(invalidEmail.exception is AuthException.InvalidEmail).isTrue()

            // Return Response.Error with InvalidPassword exception
            val invalidPassword = service.logIn(email, "")
            Truth.assertThat(invalidPassword.isError()).isTrue()
            Truth.assertThat(invalidPassword.exception is AuthException.InvalidPassword).isTrue()
        }
    }

    @Test
    fun logOut() {
        runBlocking {
            // Return Response.Success
            service.register(email, password)
            service.logIn(email, password)
            val successResponse = service.logOut()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error
            val errorValue = service.logOut()
            Truth.assertThat(errorValue.isError()).isTrue()
            Truth.assertThat(errorValue.exception is AuthException.UserNotFound).isTrue()
        }
    }

    @Test
    fun deleteAccount() {
        runBlocking {
            // Return Response.Success
            service.register(email, password)
            service.logIn(email, password)
            val successResponse = service.deleteAccount()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error
            val errorValue = service.deleteAccount()
            Truth.assertThat(errorValue.isError()).isTrue()
            Truth.assertThat(errorValue.exception is AuthException.UserNotFound).isTrue()
        }
    }

    @Test
    fun sendResetPasswordMail() {
        runBlocking {
            // Return Response.Success
            service.register(email, password)
            val successResponse = service.sendResetPasswordMail(email)
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with UserNotFound exception
            val userNotFound = service.sendResetPasswordMail(email)
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is AuthException.UserNotFound).isTrue()

            // Return Response.Error with InvalidEmail exception
            val invalidEmail = service.sendResetPasswordMail("")
            Truth.assertThat(invalidEmail.isError()).isTrue()
            Truth.assertThat(invalidEmail.exception is AuthException.InvalidEmail).isTrue()
        }
    }

    @Test
    fun validateEmail() {
        // Valid email address
        val isInvalidFalse =
            isException<AuthException.InvalidEmail> { service.validateEmail(email) }
        Truth.assertThat(isInvalidFalse).isFalse()

        // Invalid email address
        val isInvalidTrue = isException<AuthException.InvalidEmail> { service.validateEmail("") }
        Truth.assertThat(isInvalidTrue).isTrue()
    }

    @Test
    fun validatePassword() {
        // Valid password
        val isInvalidFalse =
            isException<AuthException.InvalidPassword> { service.validatePassword(password) }
        Truth.assertThat(isInvalidFalse).isFalse()

        // Invalid password
        val isInvalidTrue =
            isException<AuthException.InvalidPassword> { service.validatePassword("") }
        Truth.assertThat(isInvalidTrue).isTrue()
    }

    private fun clear() {
        runBlocking {
            service.logIn(email, password)
            service.deleteAccount()
            service.logOut()
        }
    }

    private inline fun <reified T> isException(block: () -> Unit): Boolean {
        return try {
            block.invoke()
            false
        } catch (e: Exception) {
            e is T
        }
    }

}
