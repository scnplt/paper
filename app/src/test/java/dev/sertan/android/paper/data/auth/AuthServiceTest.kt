package dev.sertan.android.paper.data.auth

import com.google.common.truth.Truth
import dev.sertan.android.paper.util.PaperException
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class AuthServiceTest {

    private val service: AuthService = FakeAuthService()
    private val email = "test@test.com"
    private val password = "12345678"

    @Test
    fun `currentUser successful`() {
        runBlocking {
            service.register(email, password)
            service.logIn(email, password)
            val response = service.currentUser()
            Truth.assertThat(response.isSuccess()).isTrue()
            Truth.assertThat(response.value?.email).isEqualTo(email)
        }
    }

    @Test
    fun `currentUser failed with UserNotFound exception before login`() {
        runBlocking {
            val response = service.currentUser()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `currentUser failed with UserNotFound exception after logout`() {
        runBlocking {
            service.register(email, password)
            service.logIn(email, password)
            service.logOut()
            val response = service.currentUser()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `currentUser failed with UserNotFound exception after deleting account`() {
        runBlocking {
            service.register(email, password)
            service.logIn(email, password)
            service.deleteAccount()
            val response = service.currentUser()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `register successful`() {
        runBlocking {
            val response = service.register(email, password)
            Truth.assertThat(response.isSuccess())
        }
    }

    @Test
    fun `register failed with UserAlreadyExists exception`() {
        runBlocking {
            service.register(email, password)
            val response = service.register(email, password)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserAlreadyExists).isTrue()
        }
    }

    @Test
    fun `logIn successful`() {
        runBlocking {
            service.register(email, password)
            val response = service.logIn(email, password)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `logIn failed with IncorrectInformation exception`() {
        runBlocking {
            val response = service.logIn(email, password)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.IncorrectInformation).isTrue()
        }
    }

    @Test
    fun `logOut successful`() {
        runBlocking {
            service.register(email, password)
            service.logIn(email, password)
            val response = service.logOut()
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `logOut failed with UserNotFound exception before login`() {
        runBlocking {
            val response = service.logOut()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `deleteAccount successful`() {
        runBlocking {
            service.register(email, password)
            service.logIn(email, password)
            val response = service.deleteAccount()
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `deleteAccount failed with UserNotFound exception`() {
        runBlocking {
            val response = service.deleteAccount()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `sendResetPasswordEmail successful`() {
        runBlocking {
            service.register(email, password)
            val response = service.sendResetPasswordEmail(email)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `sendResetPasswordEmail failed with UserNotFound exception`() {
        runBlocking {
            val response = service.sendResetPasswordEmail(email)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

}
