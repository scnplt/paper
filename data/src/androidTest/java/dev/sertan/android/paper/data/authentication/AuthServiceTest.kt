package dev.sertan.android.paper.data.authentication

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlin.math.exp
import kotlinx.coroutines.runBlocking
import org.junit.After
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

    @After
    fun teardown() {
        runBlocking {
            try {
                authService.logIn(email = testEmail, password = testPassword)
                authService.deleteAccount()
            } catch (_: Exception) {
            }
        }
    }

    @Test
    fun getCurrentUser_returnNotNull(): Unit = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)

            val user = getCurrentUser()
            assertThat(user).isNotNull()
        }
    }

    @Test
    fun getCurrentUser_returnNull_ifNotLoggedIn(): Unit = runBlocking {
        val user = authService.getCurrentUser()
        assertThat(user).isNull()
    }

    @Test
    fun getCurrentUser_returnNull_ifLoggedOut(): Unit = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)
            logOut()

            val user = getCurrentUser()
            assertThat(user).isNull()
        }
    }

    @Test
    fun getCurrentUser_returnNull_ifAccountDeleted(): Unit = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)
            deleteAccount()

            val user = getCurrentUser()
            assertThat(user).isNull()
        }
    }

    @Test
    fun register_returnTrue(): Unit = runBlocking {
        val result = authService.register(email = testEmail, password = testPassword)
        assertThat(result).isTrue()
    }

    @Test
    fun register_returnFalse_ifRegisteredBefore(): Unit = runBlocking {
        authService.register(email = testEmail, password = testPassword)

        val result = authService.register(email = testEmail, password = testPassword)
        assertThat(result).isFalse()
    }

    @Test
    fun logIn_returnTrue(): Unit = runBlocking {
        authService.register(email = testEmail, password = testPassword)

        val result = authService.logIn(email = testEmail, password = testPassword)
        assertThat(result).isTrue()
    }

    @Test
    fun logIn_returnFalse_ifNotRegistered(): Unit = runBlocking {
        val result = authService.logIn(email = testEmail, password = testPassword)
        assertThat(result).isFalse()
    }

    @Test
    fun logIn_returnFalse_ifPasswordNotCorrect(): Unit = runBlocking {
        authService.register(email = testEmail, password = testPassword)

        val result = authService.logIn(email = testEmail, password = "1$testPassword")
        assertThat(result).isFalse()
    }

    @Test
    fun logOut_returnTrue(): Unit = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)

            val result = logOut()
            assertThat(result).isTrue()
        }
    }

    @Test
    fun logOut_returnFalse_ifNotLoggedIn(): Unit = runBlocking {
        val result = authService.logOut()
        assertThat(result).isFalse()
    }

    @Test
    fun deleteAccount_returnTrue(): Unit = runBlocking {
        with(authService) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)

            val result = deleteAccount()
            assertThat(result).isTrue()
        }
    }

    @Test
    fun sendPasswordResetEmail_returnTrue(): Unit = runBlocking {
        authService.register(email = testEmail, password = testPassword)

        val result = authService.sendPasswordResetEmail(email = testEmail)
        assertThat(result).isTrue()
    }

    @Test
    fun sendPasswordResetEmail_returnFalse_ifNotRegistered(): Unit = runBlocking {
        val result = authService.sendPasswordResetEmail(email = testEmail)
        assertThat(result).isFalse()
    }
}

