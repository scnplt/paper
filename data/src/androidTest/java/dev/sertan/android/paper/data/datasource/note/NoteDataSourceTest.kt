package dev.sertan.android.paper.data.datasource.note

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.data.model.NoteEntity
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class NoteDataSourceTest {
    private val testUserUid = UUID.randomUUID().toString()

    private val testNoteEntity = NoteEntity(
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
    lateinit var localDataSource: NoteDataSource

    @Before
    fun setUp(): Unit = hiltRule.inject()

    @Test
    fun getAllAsStream_success(): Unit = runBlocking {
        val fakeNotes = (0..2).map {
            val userUid = if (it == 2) UUID.randomUUID().toString() else testUserUid
            testNoteEntity.copy(
                uid = UUID.randomUUID().toString(),
                userUid = userUid,
                title = "$it",
                content = "$it"
            )
        }.also { localDataSource.insert(notes = it.toTypedArray()) }

        val notes = localDataSource.getAllAsStream(userUid = testUserUid).firstOrNull()
        assertThat(notes).isNotEmpty()
        assertThat(notes?.size).isEqualTo(2)
        assertThat(notes).contains(fakeNotes[0])
        assertThat(notes).contains(fakeNotes[1])
        assertThat(notes).doesNotContain(fakeNotes[2])
    }

    @Test
    fun getAllAsStream_returnEmptyList(): Unit = runBlocking {
        val notes = localDataSource.getAllAsStream(userUid = testUserUid).firstOrNull()
        assertThat(notes).isEmpty()
    }

    @Test
    fun getNote_returnNotNull(): Unit = runBlocking {
        localDataSource.insert(testNoteEntity)

        val note = localDataSource.getNote(noteUid = testNoteEntity.uid)
        assertThat(note).isNotNull()
        assertThat(note).isEqualTo(testNoteEntity)
    }

    @Test
    fun getNote_returnNull_ifNotCreated(): Unit = runBlocking {
        val note = localDataSource.getNote(noteUid = testNoteEntity.uid)
        assertThat(note).isNull()
    }

    @Test
    fun getNote_returnNotNull_afterUpdate(): Unit = runBlocking {
        with(localDataSource) {
            insert(testNoteEntity)
            val updatedNote = testNoteEntity.copy(title = "new")
            update(updatedNote)

            val note = getNote(noteUid = testNoteEntity.uid)
            assertThat(note).isNotNull()
            assertThat(note).isEqualTo(updatedNote)
        }
    }

    @Test
    fun getNote_returnNull_afterDelete(): Unit = runBlocking {
        with(localDataSource) {
            insert(testNoteEntity)
            delete(testNoteEntity)

            val note = getNote(noteUid = testNoteEntity.uid)
            assertThat(note).isNull()
        }
    }

    @Test
    fun insert(): Unit = runBlocking {
        localDataSource.insert(testNoteEntity)
        val note = localDataSource.getNote(noteUid = testNoteEntity.uid)
        assertThat(note).isNotNull()
    }

    @Test
    fun delete(): Unit = runBlocking {
        with(localDataSource) {
            insert(testNoteEntity)
            delete(testNoteEntity)

            val note = getNote(noteUid = testNoteEntity.uid)
            assertThat(note).isNull()
        }
    }

    @Test
    fun update(): Unit = runBlocking {
        with(localDataSource) {
            insert(testNoteEntity)
            update(testNoteEntity.copy(title = "new"))

            val note = getNote(noteUid = testNoteEntity.uid)
            assertThat(note).isNotNull()
            assertThat(note?.title).isEqualTo("new")
        }
    }
}
