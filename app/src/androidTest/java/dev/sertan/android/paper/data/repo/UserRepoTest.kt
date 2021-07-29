package dev.sertan.android.paper.data.repo

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.data.util.PaperException
import javax.inject.Inject
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class UserRepoTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: UserRepo

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
            repo.register(email, password).last()
            repo.logIn(email, password).last()
            val successResponse = repo.currentUser.value
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value?.email).isEqualTo(email)

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.currentUser.value
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()

            // Return Response.Error with UserNotFound exception after log out
            repo.register(email, password).last()
            repo.logIn(email, password).last()
            repo.logOut().last()
            val afterLogOutResponse = repo.currentUser.value
            clear()
            Truth.assertThat(afterLogOutResponse.isError()).isTrue()
            Truth.assertThat(afterLogOutResponse.exception is PaperException.UserNotFound).isTrue()

            // Return Response.Error with UserNotFound exception after delete account
            repo.register(email, password).last()
            repo.logIn(email, password).last()
            repo.deleteAccount().last()
            val afterDeleteAccount = repo.currentUser.value
            Truth.assertThat(afterDeleteAccount.isError()).isTrue()
            Truth.assertThat(afterDeleteAccount.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun register() {
        runBlocking {
            // Return Response.Success
            val successResponse = repo.register(email, password).last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with InvalidEmailAddress exception
            val invalidEmailAddress = repo.register("", password).last()
            Truth.assertThat(invalidEmailAddress.isError()).isTrue()
            Truth.assertThat(invalidEmailAddress.exception is PaperException.InvalidEmailAddress)
                .isTrue()

            // Return Response.Error with InvalidPassword exception
            val invalidPassword = repo.register(email, "").last()
            Truth.assertThat(invalidPassword.isError()).isTrue()
            Truth.assertThat(invalidPassword.exception is PaperException.InvalidPassword).isTrue()

            // Return Response.Error with UserAlreadyExists exception
            repo.register(email, password).last()
            val userAlreadyExists = repo.register(email, password).last()
            Truth.assertThat(userAlreadyExists.isError()).isTrue()
            Truth.assertThat(userAlreadyExists.exception is PaperException.UserAlreadyExists)
                .isTrue()
        }
    }

    @Test
    fun logIn() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password).last()
            val successResponse = repo.logIn(email, password).last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with IncorrectInformation exception
            repo.register(email, password).last()
            val incorrectInformation1 = repo.logIn("test1@test.com", password).last()
            clear()
            Truth.assertThat(incorrectInformation1.isError()).isTrue()
            Truth.assertThat(incorrectInformation1.exception is PaperException.IncorrectInformation)
                .isTrue()

            // Return Response.Error with IncorrectInformation exception
            repo.register(email, password).last()
            val incorrectInformation2 = repo.logIn(email, "00000000").last()
            clear()
            Truth.assertThat(incorrectInformation2.isError()).isTrue()
            Truth.assertThat(incorrectInformation2.exception is PaperException.IncorrectInformation)
                .isTrue()

            // Return Response.Error with InvalidEmailAddress exception
            val invalidEmailAddress = repo.logIn("", password).last()
            Truth.assertThat(invalidEmailAddress.isError()).isTrue()
            Truth.assertThat(invalidEmailAddress.exception is PaperException.InvalidEmailAddress)
                .isTrue()

            // Return Response.Error with InvalidPassword exception
            val invalidPassword = repo.logIn(email, "").last()
            Truth.assertThat(invalidPassword.isError()).isTrue()
            Truth.assertThat(invalidPassword.exception is PaperException.InvalidPassword).isTrue()
        }
    }

    @Test
    fun logOut() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password).last()
            repo.logIn(email, password).last()
            val successResponse = repo.logOut().last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.logOut().last()
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun deleteAccount() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password).last()
            repo.logIn(email, password).last()
            val successResponse = repo.deleteAccount().last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.deleteAccount().last()
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()
        }
    }

    @Test
    fun sendResetPasswordMail() {
        runBlocking {
            // Return Response.Success
            repo.register(email, password).last()
            val successResponse = repo.sendResetPasswordMail(email).last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with InvalidEmailAddress exception
            val invalidEmailAddress = repo.sendResetPasswordMail("").last()
            Truth.assertThat(invalidEmailAddress.isError()).isTrue()
            Truth.assertThat(invalidEmailAddress.exception is PaperException.InvalidEmailAddress)
                .isTrue()

            // Return Response.Error with UserNotFound exception
            val userNotFound = repo.sendResetPasswordMail("test1@test.com").last()
            Truth.assertThat(userNotFound.isError()).isTrue()
            Truth.assertThat(userNotFound.exception is PaperException.UserNotFound).isTrue()
        }
    }

    private fun clear() {
        runBlocking {
            repo.logIn(email, password).last()
            repo.deleteAccount().last()
        }
    }
}
