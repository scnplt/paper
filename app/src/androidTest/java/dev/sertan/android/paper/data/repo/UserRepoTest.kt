package dev.sertan.android.paper.data.repo

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dev.sertan.android.paper.data.auth.FakeAuthService
import dev.sertan.android.paper.data.util.PaperException
import kotlinx.coroutines.runBlocking
import org.junit.Test

@SmallTest
class UserRepoTest {
    private val repo: UserRepo = UserRepo(FakeAuthService())

    private val email = "test@test.com"
    private val password = "12345678"

    @Test
    fun currentUser() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password)
            repo.logIn(email, password)
            val successResponse = repo.currentUser.value
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value?.email).isEqualTo(email)

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.currentUser.value
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()

            // Return Response.Error with UserNotFound exception after log out
            repo.register(email, password)
            repo.logIn(email, password)
            repo.logOut()
            val afterLogOutResponse = repo.currentUser.value
            clear()
            Truth.assertThat(afterLogOutResponse.isError()).isTrue()
            Truth.assertThat(afterLogOutResponse.exception is PaperException.UserNotFound).isTrue()

            // Return Response.Error with UserNotFound exception after delete account
            repo.register(email, password)
            repo.logIn(email, password)
            repo.deleteAccount()
            val afterDeleteAccount = repo.currentUser.value
            Truth.assertThat(afterDeleteAccount.isError()).isTrue()
            Truth.assertThat(afterDeleteAccount.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun register() {
        runBlocking {
            // Return Response.Success
            val successResponse = repo.register(email, password)
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with InvalidEmailAddress exception
            val invalidEmailAddress = repo.register("", password)
            Truth.assertThat(invalidEmailAddress.isError()).isTrue()
            Truth.assertThat(invalidEmailAddress.exception is PaperException.InvalidEmailAddress)
                .isTrue()

            // Return Response.Error with InvalidPassword exception
            val invalidPassword = repo.register(email, "")
            Truth.assertThat(invalidPassword.isError()).isTrue()
            Truth.assertThat(invalidPassword.exception is PaperException.InvalidPassword).isTrue()

            // Return Response.Error with UserAlreadyExists exception
            repo.register(email, password)
            val userAlreadyExists = repo.register(email, password)
            Truth.assertThat(userAlreadyExists.isError()).isTrue()
            Truth.assertThat(userAlreadyExists.exception is PaperException.UserAlreadyExists)
                .isTrue()
        }
    }

    @Test
    fun logIn() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password)
            val successResponse = repo.logIn(email, password)
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with IncorrectInformation exception
            repo.register(email, password)
            val incorrectInformation1 = repo.logIn("test1@test.com", password)
            clear()
            Truth.assertThat(incorrectInformation1.isError()).isTrue()
            Truth.assertThat(incorrectInformation1.exception is PaperException.IncorrectInformation)
                .isTrue()

            // Return Response.Error with IncorrectInformation exception
            repo.register(email, password)
            val incorrectInformation2 = repo.logIn(email, "00000000")
            clear()
            Truth.assertThat(incorrectInformation2.isError()).isTrue()
            Truth.assertThat(incorrectInformation2.exception is PaperException.IncorrectInformation)
                .isTrue()

            // Return Response.Error with InvalidEmailAddress exception
            val invalidEmailAddress = repo.logIn("", password)
            Truth.assertThat(invalidEmailAddress.isError()).isTrue()
            Truth.assertThat(invalidEmailAddress.exception is PaperException.InvalidEmailAddress)
                .isTrue()

            // Return Response.Error with InvalidPassword exception
            val invalidPassword = repo.logIn(email, "")
            Truth.assertThat(invalidPassword.isError()).isTrue()
            Truth.assertThat(invalidPassword.exception is PaperException.InvalidPassword).isTrue()
        }
    }

    @Test
    fun logOut() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password)
            repo.logIn(email, password)
            val successResponse = repo.logOut()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.logOut()
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun deleteAccount() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password)
            repo.logIn(email, password)
            val successResponse = repo.deleteAccount()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.deleteAccount()
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun sendResetPasswordMail() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password)
            val successResponse = repo.sendResetPasswordMail(email)
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with InvalidEmailAddress exception
            val invalidEmailAddress = repo.sendResetPasswordMail("")
            Truth.assertThat(invalidEmailAddress.isError()).isTrue()
            Truth.assertThat(invalidEmailAddress.exception is PaperException.InvalidEmailAddress)
                .isTrue()

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.sendResetPasswordMail("test1@test.com")
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()
        }
    }

    private fun clear() {
        runBlocking {
            repo.logIn(email, password)
            repo.deleteAccount()
        }
    }
}
