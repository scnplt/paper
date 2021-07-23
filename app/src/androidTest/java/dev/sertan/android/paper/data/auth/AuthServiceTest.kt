package dev.sertan.android.paper.data.auth

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
class AuthServiceTest {
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
    fun register() {
        runBlocking {
            val successResult = service.register(email, password)
            Truth.assertThat(successResult.isSuccess()).isTrue()

            val errorResult = service.register(email, password)
            clearExistUser()
            Truth.assertThat(errorResult.isError()).isTrue()
            Truth.assertThat(errorResult.exception is AuthException.UserAlreadyExist).isTrue()

            val errorValidateEmail = service.register("", password)
            Truth.assertThat(errorValidateEmail.isError()).isTrue()
            Truth.assertThat(errorValidateEmail.exception is AuthException.InvalidEmail).isTrue()

            val errorValidatePassword = service.register(email, "")
            Truth.assertThat(errorValidatePassword.isError()).isTrue()
            Truth.assertThat(errorValidatePassword.exception is AuthException.InvalidPassword)
                .isTrue()
        }
    }

    @Test
    fun currentUser() {
        runBlocking {
            val nullResult = service.currentUser()
            Truth.assertThat(nullResult.isSuccess()).isTrue()
            Truth.assertThat(nullResult.value).isNull()

            service.register(email, password)
            service.logIn(email, password)
            val userResult = service.currentUser()
            clearExistUser()
            Truth.assertThat(userResult.isSuccess()).isTrue()
            Truth.assertThat(userResult.value).isNotNull()
            Truth.assertThat(userResult.value?.email).isEqualTo(email)
        }
    }

    @Test
    fun logIn() {
        runBlocking {
            val errorResult = service.logIn(email, password)
            Truth.assertThat(errorResult.isError()).isTrue()
            Truth.assertThat(errorResult.exception is AuthException.IncorrectInformation).isTrue()

            service.register(email, password)
            val successResult = service.logIn(email, password)
            clearExistUser()
            Truth.assertThat(successResult.isSuccess()).isTrue()
            Truth.assertThat(successResult.value).isTrue()
        }
    }

    @Test
    fun logOut() {
        runBlocking {
            val falseResult = service.logOut()
            Truth.assertThat(falseResult.isSuccess()).isTrue()
            Truth.assertThat(falseResult.value).isFalse()

            service.register(email, password)
            service.logIn(email, password)
            val trueResult = service.logOut()
            clearExistUser()
            Truth.assertThat(trueResult.isSuccess()).isTrue()
            Truth.assertThat(trueResult.value).isTrue()
        }
    }

    @Test
    fun deleteAccount() {
        runBlocking {
            val falseResult = service.deleteAccount()
            Truth.assertThat(falseResult.isSuccess()).isTrue()
            Truth.assertThat(falseResult.value).isFalse()

            service.register(email, password)
            service.logIn(email, password)
            val trueResult = service.deleteAccount()
            clearExistUser()
            Truth.assertThat(trueResult.isSuccess()).isTrue()
            Truth.assertThat(trueResult.value).isTrue()
        }
    }

    @Test
    fun sendResetPasswordEmail() {
        runBlocking {
            val errorResult = service.sendResetPasswordMail(email)
            Truth.assertThat(errorResult.isError()).isTrue()
            Truth.assertThat(errorResult.exception is AuthException.UserNotFound).isTrue()

            val errorValidateEmail = service.sendResetPasswordMail("")
            Truth.assertThat(errorValidateEmail.isError()).isTrue()
            Truth.assertThat(errorValidateEmail.exception is AuthException.InvalidEmail).isTrue()

            service.register(email, password)
            val successResult = service.sendResetPasswordMail(email)
            clearExistUser()
            Truth.assertThat(successResult.isSuccess())
            Truth.assertThat(successResult.value).isTrue()
        }
    }

    @Test
    fun validateEmail() {
        // Valid email
        val step1 = isException<AuthException.InvalidEmail> { service.validateEmail(email) }
        Truth.assertThat(step1).isFalse()

        // Invalid email
        val step2 = isException<AuthException.InvalidEmail> { service.validateEmail("") }
        Truth.assertThat(step2).isTrue()
    }

    @Test
    fun validatePassword() {
        // Valid password
        val step1 =
            isException<AuthException.InvalidPassword> { service.validatePassword(password) }
        Truth.assertThat(step1).isFalse()

        // Invalid password
        val step2 = isException<AuthException.InvalidPassword> { service.validatePassword("") }
        Truth.assertThat(step2).isTrue()
    }

    private inline fun <reified T> isException(block: () -> Unit): Boolean {
        return try {
            block.invoke()
            false
        } catch (e: Exception) {
            e is T
        }
    }

    private fun clearExistUser() {
        runBlocking {
            service.logIn(email, password)
            service.deleteAccount()
        }
    }
}
