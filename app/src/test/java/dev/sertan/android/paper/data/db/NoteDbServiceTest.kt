package dev.sertan.android.paper.data.db

import com.google.common.truth.Truth
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.PaperException
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class NoteDbServiceTest {

    private val service: DbService<Note> = FakeNoteDbService()
    private val userUid = "1"
    private val note = Note(userUid)

    @Test
    fun `create successful`() {
        runBlocking {
            val response = service.create(note)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `delete successful`() {
        runBlocking {
            service.create(note)
            val response = service.delete(note)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `delete failed with DataNotFound exception`() {
        runBlocking {
            val response = service.delete(note)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `update successful`() {
        runBlocking {
            service.create(note)
            val response = service.update(note.apply { title = "Updated" })
            Truth.assertThat(response.isSuccess())
        }
    }

    @Test
    fun `update failed with DataNotFound exception`() {
        runBlocking {
            val response = service.update(note.apply { title = "Updated" })
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `getData successful`() {
        runBlocking {
            service.create(note)
            val response = service.getData(note.uid)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `getData failed with DataNotFound exception`() {
        runBlocking {
            val response = service.getData(note.uid)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `getAllData successful`() {
        runBlocking {
            val secondNote = Note(userUid)
            service.create(note)
            service.create(secondNote)
            val response = service.getAllData(userUid).take(2).last()
            Truth.assertThat(response.value).contains(note)
            Truth.assertThat(response.value).contains(secondNote)
        }
    }

    @Test
    fun `getAllData failed with DataNotFound exception`() {
        runBlocking {
            val response = service.getAllData(userUid).take(2).last()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

}
