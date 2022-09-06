package dev.sertan.android.paper.data.database

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.data.model.DatabaseNote
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class NoteDatabaseServiceTest {

    private val testUserUid = UUID.randomUUID().toString()
    private val testDatabaseNote = DatabaseNote(
        uid = UUID.randomUUID().toString(),
        userUid = testUserUid,
        title = null,
        content = null,
        createDateMillis = 0,
        updateDateMillis = 0
    )

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var noteDatabaseService: NoteDatabaseService

    @Before
    fun setUp() = hiltRule.inject()

    @Test
    fun create_success() = runBlocking {
        val result = noteDatabaseService.create(databaseNote = testDatabaseNote)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.exception).isNull()
        assertThat(result.data).isTrue()
    }

    @Test
    fun update_success() = runBlocking {
        with(noteDatabaseService) {
            create(databaseNote = testDatabaseNote)

            val result = update(databaseNote = testDatabaseNote.copy(title = "new"))
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun delete_success() = runBlocking {
        with(noteDatabaseService) {
            create(databaseNote = testDatabaseNote)

            val result = delete(databaseNote = testDatabaseNote)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isTrue()
        }
    }

    @Test
    fun delete_failure_ifNotCreated() = runBlocking {
        val result = noteDatabaseService.delete(databaseNote = testDatabaseNote)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exception).isNotNull()
        assertThat(result.data).isNull()
    }

    @Test
    fun getNote_success() = runBlocking {
        with(noteDatabaseService) {
            create(databaseNote = testDatabaseNote)

            val result = getNote(noteUid = testDatabaseNote.uid)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isEqualTo(testDatabaseNote)
        }
    }

    @Test
    fun getNote_nullValue_ifNotCreated() = runBlocking {
        val result = noteDatabaseService.getNote(noteUid = testDatabaseNote.uid)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.exception).isNull()
        assertThat(result.data).isNull()
    }

    @Test
    fun getNote_success_afterUpdate() = runBlocking {
        with(noteDatabaseService) {
            create(databaseNote = testDatabaseNote)
            val newDatabaseNote = testDatabaseNote.copy(title = "new", content = "new")
            update(databaseNote = newDatabaseNote)

            val result = getNote(noteUid = testDatabaseNote.uid)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isEqualTo(newDatabaseNote)
        }
    }

    @Test
    fun getNote_nullValue_afterDelete() = runBlocking {
        with(noteDatabaseService) {
            create(databaseNote = testDatabaseNote)
            delete(databaseNote = testDatabaseNote)

            val result = getNote(noteUid = testDatabaseNote.uid)
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun getNotes_success() = runBlocking {
        val fakeDatabaseNotes = (0..2).map {
            val userUid = if (it == 2) UUID.randomUUID().toString() else testUserUid
            testDatabaseNote.copy(
                uid = UUID.randomUUID().toString(),
                title = "$it",
                content = "$it",
                userUid = userUid
            )
        }

        with(noteDatabaseService) {
            create(databaseNote = testDatabaseNote)
            fakeDatabaseNotes.forEach { create(databaseNote = it) }

            val result = getNotes(userUid = testUserUid).take(10).last()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.exception).isNull()
            assertThat(result.data?.size).isEqualTo(3)
            assertThat(result.data).contains(testDatabaseNote)
            assertThat(result.data).contains(fakeDatabaseNotes[0])
            assertThat(result.data).contains(fakeDatabaseNotes[1])
            assertThat(result.data).doesNotContain(fakeDatabaseNotes[2])
        }
    }

    @Test
    fun getNotes_emptyList() = runBlocking {
        val result = noteDatabaseService.getNotes(userUid = testUserUid).take(10).last()
        assertThat(result.isSuccess).isTrue()
        assertThat(result.exception).isNull()
        assertThat(result.data).isEmpty()
    }
}
