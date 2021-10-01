package dev.sertan.android.paper.data.auth

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.util.PaperException
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
internal class AuthServiceTest {
    private val email = "test@test.com"
    private val password = "01234567"

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authService: AuthService

    @Before
    fun setUp() = hiltRule.inject()

    @After
    fun teardown() = clean()

    @Test
    fun currentUser_success() = runBlocking {
        authService.register(email, password)
        authService.logIn(email, password)
        val response = authService.currentUser()

        Truth.assertThat(response.isSuccess()).isTrue()
        Truth.assertThat(response.value?.email).isEqualTo(email)
    }

    @Test
    fun currentUser_failureWithUserNotFoundException_ifNotLoggedIn() = runBlocking {
        val response = authService.currentUser()

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
    }

    @Test
    fun currentUser_failureWithUserNotFoundException_ifLoggedOut() = runBlocking {
        authService.register(email, password)
        authService.logIn(email, password)
        authService.logOut()
        val response = authService.currentUser()

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
    }

    @Test
    fun currentUser_failureWithUserNotFoundException_ifDeletedAccount() = runBlocking {
        authService.register(email, password)
        authService.logIn(email, password)
        authService.deleteAccount()
        val response = authService.currentUser()

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
    }

    @Test
    fun register_success() = runBlocking {
        val response = authService.register(email, password)

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun register_failureWithUserAlreadyExistsException_ifRegisteredBefore() = runBlocking {
        authService.register(email, password)
        val response = authService.register(email, password)

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.UserAlreadyExists).isTrue()
    }

    @Test
    fun logIn_success() = runBlocking {
        authService.register(email, password)
        val response = authService.logIn(email, password)

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun logIn_failureWithUserNotFoundException_ifNotRegistered() = runBlocking {
        val response = authService.logIn(email, password)

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
    }

    @Test
    fun logIn_failureIncorrectInformationException_ifPasswordNotCorrect() = runBlocking {
        authService.register(email, password)
        val response = authService.logIn(email, "1$password")

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.IncorrectInformation).isTrue()
    }

    @Test
    fun logOut_success() = runBlocking {
        authService.register(email, password)
        authService.logIn(email, password)
        val response = authService.logOut()

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun deleteAccount_success() = runBlocking {
        authService.register(email, password)
        authService.logIn(email, password)
        val response = authService.deleteAccount()

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun sendResetPasswordEmail_success() = runBlocking {
        authService.register(email, password)
        val response = authService.sendResetPasswordEmail(email)

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun sendResetPasswordEmail_failureWithUserNotFoundException_ifNotRegistered() = runBlocking {
        val response = authService.sendResetPasswordEmail(email)

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.UserNotFound).isTrue()
    }

    private fun clean() {
        runBlocking {
            authService.logIn(email, password)
            authService.deleteAccount()
        }
    }

}
