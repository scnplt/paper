package dev.sertan.android.paper.data.authentication

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class AuthServiceTest {
    private val testEmail = "test@test.com"
    private val testPassword = "01234567"

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
        authService.register(email = testEmail, password = testPassword)
        authService.logIn(email = testEmail, password = testPassword)
        val response = authService.currentUser()

        Truth.assertThat(response.isSuccess).isTrue()
        Truth.assertThat(response.value?.email).isEqualTo(testEmail)
    }

    @Test
    fun currentUser_nullValue_ifNotLoggedIn() = runBlocking {
        val response = authService.currentUser()

        Truth.assertThat(response.isSuccess).isTrue()
        Truth.assertThat(response.value).isNull()
        Truth.assertThat(response.exception).isNull()
    }

    @Test
    fun currentUser_nullValue_ifLoggedOut() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        authService.logIn(email = testEmail, password = testPassword)
        authService.logOut()
        val response = authService.currentUser()

        Truth.assertThat(response.isSuccess).isTrue()
        Truth.assertThat(response.value).isNull()
        Truth.assertThat(response.exception).isNull()
    }

    @Test
    fun currentUser_nullValue_ifDeletedAccount() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        authService.logIn(email = testEmail, password = testPassword)
        authService.deleteAccount()
        val response = authService.currentUser()

        Truth.assertThat(response.isSuccess).isTrue()
        Truth.assertThat(response.value).isNull()
        Truth.assertThat(response.exception).isNull()
    }

    @Test
    fun register_success() = runBlocking {
        val response = authService.register(email = testEmail, password = testPassword)

        Truth.assertThat(response.isSuccess).isTrue()
    }

    @Test
    fun register_failure_ifRegisteredBefore() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        val response = authService.register(email = testEmail, password = testPassword)

        Truth.assertThat(response.isFailure).isTrue()
        Truth.assertThat(response.exception).isNotNull()
        Truth.assertThat(response.value).isNull()
    }

    @Test
    fun logIn_success() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        val response = authService.logIn(email = testEmail, password = testPassword)

        Truth.assertThat(response.isSuccess).isTrue()
    }

    @Test
    fun logIn_failure_ifNotRegistered() = runBlocking {
        val response = authService.logIn(email = testEmail, password = testPassword)

        Truth.assertThat(response.isFailure).isTrue()
        Truth.assertThat(response.exception).isNotNull()
        Truth.assertThat(response.value).isNull()
    }

    @Test
    fun logIn_failure_ifPasswordNotCorrect() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        val response = authService.logIn(testEmail, "1$testPassword")

        Truth.assertThat(response.isFailure).isTrue()
        Truth.assertThat(response.exception).isNotNull()
        Truth.assertThat(response.value).isNull()
    }

    @Test
    fun logOut_success() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        authService.logIn(email = testEmail, password = testPassword)
        val response = authService.logOut()

        Truth.assertThat(response.isSuccess).isTrue()
    }

    @Test
    fun deleteAccount_success() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        authService.logIn(email = testEmail, password = testPassword)
        val response = authService.deleteAccount()

        Truth.assertThat(response.isSuccess).isTrue()
    }

    @Test
    fun sendResetPasswordEmail_success() = runBlocking {
        authService.register(email = testEmail, password = testPassword)
        val response = authService.sendResetPasswordEmail(testEmail)

        Truth.assertThat(response.isSuccess).isTrue()
    }

    @Test
    fun sendResetPasswordEmail_failure_ifNotRegistered() = runBlocking {
        val response = authService.sendResetPasswordEmail(testEmail)

        Truth.assertThat(response.isFailure).isTrue()
        Truth.assertThat(response.exception).isNotNull()
        Truth.assertThat(response.value).isNull()
    }

    private fun clean() {
        runBlocking {
            authService.logIn(email = testEmail, password = testPassword)
            authService.deleteAccount()
        }
    }

}
