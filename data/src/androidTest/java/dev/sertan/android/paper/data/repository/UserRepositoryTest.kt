package dev.sertan.android.paper.data.repository

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.model.Password
import dev.sertan.android.paper.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class UserRepositoryTest {

    private val testEmail = Email.create("test@test.com")
    private val testPassword = Password.create("12345678")

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() = hiltRule.inject()

    @Test
    fun getCurrentUser_success() = runBlocking {
        with(userRepository) {
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
        with(userRepository) {
            register(email = testEmail, password = testPassword)

            val result = getCurrentUser()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun getCurrentUser_nullValue_ifLoggedOut() = runBlocking {
        with(userRepository) {
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
        with(userRepository) {
            register(email = testEmail, password = testPassword)
            logIn(email = testEmail, password = testPassword)
            deleteAccount()

            val result = getCurrentUser()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun register_success() = runBlocking {
        val result = userRepository.register(email = testEmail, password = testPassword)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.exception).isNull()
        assertThat(result.data).isTrue()
    }

    @Test
    fun register_failure_ifRegisteredBefore() = runBlocking {
        with(userRepository) {
            register(email = testEmail, password = testPassword)

            val result = register(email = testEmail, password = testPassword)
            assertThat(result.isFailure).isTrue()
            assertThat(result.exception).isNotNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun logIn_success() = runBlocking {
        with(userRepository) {
            register(email = testEmail, password = testPassword)

            val result = logIn(email = testEmail, password = testPassword)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun logIn_failure_ifNotRegistered() = runBlocking {
        val result = userRepository.logIn(email = testEmail, password = testPassword)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exception).isNotNull()
        assertThat(result.data).isNull()
    }

    @Test
    fun logIn_failure_ifPasswordNotCorrect() = runBlocking {
        with(userRepository) {
            register(email = testEmail, password = testPassword)

            val result = logIn(email = testEmail, password = Password.create(pwd = "1$testPassword"))
            assertThat(result.isFailure).isTrue()
            assertThat(result.exception).isNotNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun sendPasswordResetEmail_success() = runBlocking {
        with(userRepository) {
            register(email = testEmail, password = testPassword)

            val result = sendPasswordResetEmail(email = testEmail)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun sendPasswordResetEmail_failure_ifNotRegistered() = runBlocking {
        val result = userRepository.sendPasswordResetEmail(email = testEmail)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exception).isNotNull()
        assertThat(result.data).isNull()
    }
}
