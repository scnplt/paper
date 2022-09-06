package dev.sertan.android.paper.data.authentication

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class AuthServiceTest {

    private val testEmail = "test@test.com"
    private val testPassword = "12345678"

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authService: AuthService

    @Before
    fun setUp() = hiltRule.inject()

    @Test
    fun getCurrentUser_success() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)

            val result = getCurrentUser()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data?.email).isEqualTo(testEmail)
        }
    }

    @Test
    fun getCurrentUser_nullValue_ifNotLoggedIn() = runBlocking {
        with(authService) {
            val result = getCurrentUser()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun getCurrentUser_nullValue_ifLoggedOut() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)
            logOut()

            val result = getCurrentUser()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun getCurrentUser_nullValue_ifAccountDeleted() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)
            deleteAccount()

            val currentUserResponse = getCurrentUser()
            assertThat(currentUserResponse.isSuccess).isTrue()
            assertThat(currentUserResponse.exception).isNull()
            assertThat(currentUserResponse.data).isNull()
        }
    }

    @Test
    fun register_success() = runBlocking {
        val result = authService.register(email = testEmail, password = testPassword)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.exception).isNull()
        assertThat(result.data).isTrue()
    }

    @Test
    fun register_failure_ifRegisteredBefore() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)

            val result = register(email = testEmail, password = testPassword)
            assertThat(result.isFailure).isTrue()
            assertThat(result.exception).isNotNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun logIn_success() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)

            val result = logIn(email = testEmail, password = testPassword)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun logIn_failure_ifNotRegistered() = runBlocking {
        val result = authService.logIn(email = testEmail, password = testPassword)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exception).isNotNull()
        assertThat(result.data).isNull()
    }

    @Test
    fun logIn_failure_ifPasswordNotCorrect() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)

            val result = logIn(email = testEmail, password = "1$testPassword")
            assertThat(result.isFailure).isTrue()
            assertThat(result.exception).isNotNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun logOut_success() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)

            val result = logOut()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun logOut_failure_ifNotLoggedIn() = runBlocking {
        val result = authService.logOut()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exception).isNotNull()
        assertThat(result.data).isNull()
    }

    @Test
    fun deleteAccount_success() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)

            val result = deleteAccount()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun deleteAccount_failure_ifNotLoggedIn() = runBlocking {
        val result = authService.deleteAccount()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exception).isNotNull()
        assertThat(result.data).isNull()
    }

    @Test
    fun sendPasswordResetEmail_success() = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)

            val result = sendPasswordResetEmail(email = testEmail)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun sendPasswordResetEmail_failure_ifNotRegistered() = runBlocking {
        val result = authService.sendPasswordResetEmail(email = testEmail)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exception).isNotNull()
        assertThat(result.data).isNull()
    }
}

