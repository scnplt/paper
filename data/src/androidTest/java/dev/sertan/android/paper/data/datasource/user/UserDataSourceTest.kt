package dev.sertan.android.paper.data.datasource.user

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class UserDataSourceTest {
    private val testUserUid = UUID.randomUUID().toString()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userDataSource: UserDataSource

    @Before
    fun setUp() = hiltRule.inject()

    @Test
    fun getUserUidAsStream_returnNull(): Unit = runBlocking {
        val userUid = userDataSource.getUserUidAsStream().firstOrNull()
        assertThat(userUid).isNull()
    }

    @Test
    fun getUserUidAsStream_returnNotNull_ifSetDataBefore(): Unit = runBlocking {
        userDataSource.setUserUid(userUid = testUserUid)
        val userUid = userDataSource.getUserUidAsStream().firstOrNull()
        assertThat(userUid).isEqualTo(testUserUid)
    }

    @Test
    fun setUserUid_returnTrue(): Unit = runBlocking {
        val result = userDataSource.setUserUid(testUserUid)
        assertThat(result).isTrue()
    }
}
