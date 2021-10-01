package dev.sertan.android.paper.data.db

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.PaperException
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
internal class NoteDbServiceTest {
    private val userUid = "1"
    private val note = Note(userUid = userUid)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dbService: DbService<Note>

    @Before
    fun setUp() = hiltRule.inject()

    @After
    fun teardown() {
        runBlocking { dbService.delete(note) }
    }

    @Test
    fun create_success() = runBlocking {
        val response = dbService.create(note)

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun delete_success() = runBlocking {
        dbService.create(note)
        val response = dbService.delete(note)

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun delete_failureWithDataNotFoundException_ifNotCreated() = runBlocking {
        val response = dbService.delete(note)

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
    }

    @Test
    fun update_success() = runBlocking {
        dbService.create(note)
        val response = dbService.update(note.apply { title = "updated" })

        Truth.assertThat(response.isSuccess()).isTrue()
    }

    @Test
    fun update_failureWithDataNotFoundException_ifNotCreated() = runBlocking {
        val response = dbService.update(note.apply { title = "updated" })

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
    }

    @Test
    fun getData_success() = runBlocking {
        dbService.create(note)
        val response = dbService.getData(note.uid)

        Truth.assertThat(response.isSuccess()).isTrue()
        Truth.assertThat(response.value).isEqualTo(note)
    }

    @Test
    fun getData_failureWithDataNotFoundException_ifNotCreated() = runBlocking {
        val response = dbService.getData(note.uid)

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
    }

    @Test
    fun getAllData_success() = runBlocking {
        val secondNote = Note(userUid = userUid)
        dbService.create(note)
        dbService.create(secondNote)
        val response = dbService.getAllData(userUid).take(2).last()
        dbService.delete(secondNote)

        Truth.assertThat(response.isSuccess()).isTrue()
        Truth.assertThat(response.value).contains(note)
        Truth.assertThat(response.value).contains(secondNote)
    }

    @Test
    fun getAllData_failureWithDataNotFoundException_ifNotCreated() = runBlocking {
        val response = dbService.getAllData(userUid).take(2).last()

        Truth.assertThat(response.isFailure()).isTrue()
        Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
    }

}
