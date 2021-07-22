package dev.sertan.android.paper.data.auth

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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
            val step1 = service.register(email, password)
            Truth.assertThat(step1.isSuccess()).isTrue()
            Truth.assertThat(step1.value?.email).isEqualTo(email)

            // User has already been created.
            val step2 = service.register(email, password)
            clearExistUser()
            Truth.assertThat(step2.isError()).isTrue()
            Truth.assertThat(step2.exception is AuthException.UserAlreadyExist).isTrue()
        }
    }

    @Test
    fun currentUser() {
        runBlocking {
            // User wasn't created and logged in.
            val step1 = service.currentUser()
            clearExistUser()
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is AuthException.UserNotFound).isTrue()

            // User was created but not logged in.
            service.register(email, password)
            val step2 = service.currentUser()
            clearExistUser()
            Truth.assertThat(step2.isError()).isTrue()
            Truth.assertThat(step2.exception is AuthException.UserNotFound).isTrue()

            // User was created and logged in.
            service.register(email, password)
            service.logIn(email, password)
            val step3 = service.currentUser()
            clearExistUser()
            Truth.assertThat(step3.isSuccess()).isTrue()
            Truth.assertThat(step3.value?.email).isEqualTo(email)
        }
    }

    @Test
    fun logIn() {
        runBlocking {
            // User wasn't created.
            val step1 = service.logIn(email, password)
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is AuthException.IncorrectInformation).isTrue()

            // User was created.
            service.register(email, password)
            val step2 = service.logIn(email, password)
            clearExistUser()
            Truth.assertThat(step2.isSuccess()).isTrue()
            Truth.assertThat(step2.value?.email).isEqualTo(email)
        }
    }

    @Test
    fun logOut() {
        runBlocking {
            // Not logged in.
            val step1 = service.logOut()
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is AuthException.UserNotFound).isTrue()

            // Logged in.
            service.register(email, password)
            service.logIn(email, password)
            val step2 = service.logOut()
            clearExistUser()
            Truth.assertThat(step2.isSuccess()).isTrue()
        }
    }

    @Test
    fun deleteAccount() {
        runBlocking {
            // User wasn't created and logged in.
            val step1 = service.deleteAccount()
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is AuthException.UserNotFound).isTrue()

            // User was created but not signed in.
            service.register(email, password)
            val step2 = service.deleteAccount()
            clearExistUser()
            Truth.assertThat(step2.isError()).isTrue()
            Truth.assertThat(step2.exception is AuthException.UserNotFound).isTrue()

            // User was created and logged in.
            service.register(email, password)
            service.logIn(email, password)
            val step3 = service.deleteAccount()
            Truth.assertThat(step3.isSuccess()).isTrue()
        }
    }

    @Test
    fun sendResetPasswordEmail() {
        runBlocking {
            // User wasn't created.
            val step1 = service.sendResetPasswordMail(email)
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is AuthException.UserNotFound).isTrue()

            // User was created.
            service.register(email, password)
            val step2 = service.sendResetPasswordMail(email)
            clearExistUser()
            Truth.assertThat(step2.isSuccess()).isTrue()
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
