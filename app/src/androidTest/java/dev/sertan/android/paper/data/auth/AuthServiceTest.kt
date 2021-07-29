package dev.sertan.android.paper.data.auth

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.data.util.PaperException
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class AuthServiceTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var service: AuthService

    private val email = "test@test.com"
    private val password = "12345678"

    @Before
    fun setup() {
        hiltRule.inject()
    }

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
            Truth.assertThat(errorResponse.exception is PaperException.UserNotFound).isTrue()
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
            val userAlreadyExists = service.register(email, password)
            clear()
            Truth.assertThat(userAlreadyExists.isError()).isTrue()
            Truth.assertThat(userAlreadyExists.exception is PaperException.UserAlreadyExists)
                .isTrue()
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
            Truth.assertThat(incorrectInformation1.exception is PaperException.IncorrectInformation)
                .isTrue()

            // Return Response.Error with IncorrectInformation exception
            service.register(email, password)
            val incorrectInformation2 = service.logIn("aaaa@test.com", password)
            clear()
            Truth.assertThat(incorrectInformation2.isError()).isTrue()
            Truth.assertThat(incorrectInformation2.exception is PaperException.IncorrectInformation)
                .isTrue()
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
            Truth.assertThat(errorValue.exception is PaperException.UserNotFound).isTrue()
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
            Truth.assertThat(errorValue.exception is PaperException.UserNotFound).isTrue()
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
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()
        }
    }

    private fun clear() {
        runBlocking {
            service.logIn(email, password)
            service.deleteAccount()
            service.logOut()
        }
    }

}
