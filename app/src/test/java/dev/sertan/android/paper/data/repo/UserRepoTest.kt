package dev.sertan.android.paper.data.repo

import com.google.common.truth.Truth
import dev.sertan.android.paper.data.auth.FakeAuthService
import dev.sertan.android.paper.util.PaperException
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class UserRepoTest {

    private val repo = UserRepo(FakeAuthService())
    private val email = "test@test.com"
    private val password = "12345678"

    @Test
    fun `currentUser successful`() {
        runBlocking {
            repo.register(email, password)
            repo.logIn(email, password)
            val response = repo.currentUser.value
            Truth.assertThat(response.isSuccess()).isTrue()
            Truth.assertThat(response.value?.email).isEqualTo(email)
        }
    }

    @Test
    fun `currentUser idle`() {
        runBlocking {
            val response = repo.currentUser.value
            Truth.assertThat(response.isIdle()).isTrue()
        }
    }

    @Test
    fun `currentUser failed with UserNotFound exception after logout`() {
        runBlocking {
            repo.register(email, password)
            repo.logIn(email, password)
            repo.logOut()
            val response = repo.currentUser.value
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `currentUser failed with UserNotFound exception after deleting account`() {
        runBlocking {
            repo.register(email, password)
            repo.logIn(email, password)
            repo.deleteAccount()
            val response = repo.currentUser.value
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `register successful`() {
        runBlocking {
            val response = repo.register(email, password)
            Truth.assertThat(response.isSuccess())
        }
    }

    @Test
    fun `register failed with UserAlreadyExists exception`() {
        runBlocking {
            repo.register(email, password)
            val response = repo.register(email, password)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserAlreadyExists).isTrue()
        }
    }

    @Test
    fun `logIn successful`() {
        runBlocking {
            repo.register(email, password)
            val response = repo.logIn(email, password)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `logIn failed with IncorrectInformation exception`() {
        runBlocking {
            val response = repo.logIn(email, password)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.IncorrectInformation).isTrue()
        }
    }

    @Test
    fun `logOut successful`() {
        runBlocking {
            repo.register(email, password)
            repo.logIn(email, password)
            val response = repo.logOut()
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `logOut failed with UserNotFound exception before login`() {
        runBlocking {
            val response = repo.logOut()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `deleteAccount successful`() {
        runBlocking {
            repo.register(email, password)
            repo.logIn(email, password)
            val response = repo.deleteAccount()
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `deleteAccount failed with UserNotFound exception`() {
        runBlocking {
            val response = repo.deleteAccount()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun `sendResetPasswordEmail successful`() {
        runBlocking {
            repo.register(email, password)
            val response = repo.sendResetPasswordEmail(email)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `sendResetPasswordEmail failed with UserNotFound exception`() {
        runBlocking {
            val response = repo.sendResetPasswordEmail(email)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
        }
    }

}
