package dev.sertan.android.paper.data.repo

import com.google.common.truth.Truth
import dev.sertan.android.paper.data.db.FakePaperDbService
import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.data.util.PaperException
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class PaperRepoTest {

    private val repo = PaperRepo(FakePaperDbService())
    private val userUid = "1"
    private val paper = Paper(userUid)

    @Test
    fun `create successful`() {
        runBlocking {
            val response = repo.create(paper)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `delete successful`() {
        runBlocking {
            repo.create(paper)
            val response = repo.delete(paper)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `delete failed with DataNotFound exception`() {
        runBlocking {
            val response = repo.delete(paper)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `update successful`() {
        runBlocking {
            repo.create(paper)
            val response = repo.update(paper.apply { title = "Updated" })
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `update failed with DataNotFound exception`() {
        runBlocking {
            val response = repo.update(paper.apply { title = "Updated" })
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `getData successful`() {
        runBlocking {
            repo.create(paper)
            val response = repo.getData(paper.uid)
            Truth.assertThat(response.isSuccess()).isTrue()
        }
    }

    @Test
    fun `getData failed with DataNotFound exception`() {
        runBlocking {
            val response = repo.getData(paper.uid)
            Truth.assertThat(response.isFailure()).isTrue()
            Truth.assertThat(response.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun `getAllData successful`() {
        runBlocking {
            val secondPaper = Paper(userUid)
            repo.create(paper)
            repo.create(secondPaper)
            val response = repo.getAllData(userUid).take(2).last()
            Truth.assertThat(response.value).contains(paper)
            Truth.assertThat(response.value).contains(secondPaper)
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
