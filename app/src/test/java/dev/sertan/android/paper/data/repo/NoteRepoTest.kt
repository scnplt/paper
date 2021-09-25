package dev.sertan.android.paper.data.repo

import com.google.common.truth.Truth
import dev.sertan.android.paper.data.db.FakeNoteDbService
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.util.PaperException
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class NoteRepoTest {

    private val repo = NoteRepo(FakeNoteDbService())
    private val userUid = "1"
    private val note = Note(userUid = userUid)

    @Test
    fun `create successful`() {
        runBlocking {
            val response = repo.create(note)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `delete successful`() {
        runBlocking {
            repo.create(note)
            val response = repo.delete(note)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `delete failed with DataNotFound exception`() {
        runBlocking {
            val response = repo.delete(note)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `update successful`() {
        runBlocking {
            repo.create(note)
            val response = repo.update(note.apply { title = "Updated" })
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `update failed with DataNotFound exception`() {
        runBlocking {
            val response = repo.update(note.apply { title = "Updated" })
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `getData successful`() {
        runBlocking {
            repo.create(note)
            val response = repo.getData(note.uid)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `getData failed with DataNotFound exception`() {
        runBlocking {
            val response = repo.getData(note.uid)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `getAllData successful`() {
        runBlocking {
            val secondNote = Note(userUid = userUid)
            repo.create(note)
            repo.create(secondNote)
            val response = repo.getAllData(userUid).take(2).last()
            Truth.assertThat(response.value).contains(note)
            Truth.assertThat(response.value).contains(secondNote)
        }
    }

    @Test
    fun `getAllData failed with DataNotFound exception`() {
        runBlocking {
            val response = repo.getAllData(userUid).take(2).last()
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

}
