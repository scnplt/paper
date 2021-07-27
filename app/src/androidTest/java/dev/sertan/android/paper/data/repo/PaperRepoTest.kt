package dev.sertan.android.paper.data.repo

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dev.sertan.android.paper.data.db.FakePaperDbService
import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.data.util.PaperException
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Test

@SmallTest
class PaperRepoTest {
    private val repo: PaperRepo = PaperRepo(FakePaperDbService())

    private val userUid = "1"
    private val paper = Paper(userUid)

    @Test
    fun create() {
        runBlocking {
            // Return Response.Success
            val successResponse = repo.create(paper).last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value).isEqualTo(paper)
        }
    }

    @Test
    fun delete() {
        runBlocking {
            // Return Response.Success
            repo.create(paper).last()
            val successResponse = repo.delete(paper).last()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with DataNotFound exception
            val dataNotFoundException = repo.delete(paper).last()
            Truth.assertThat(dataNotFoundException.isError()).isTrue()
            Truth.assertThat(dataNotFoundException.exception is PaperException.DataNotFound)
                .isTrue()
        }
    }

    @Test
    fun update() {
        runBlocking {
            // Return Response.Success
            repo.create(paper).last()
            val successResponse = repo.update(paper.apply { title = "Updated" }).last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()

            // Return Response.Error with DataNotFound exception
            val dataNotFound = repo.update(paper.apply { title = "Updated" }).last()
            Truth.assertThat(dataNotFound.isError()).isTrue()
            Truth.assertThat(dataNotFound.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun getData() {
        runBlocking {
            // Return Response.Success
            repo.create(paper).last()
            val successResponse = repo.getData(paper.uid).last()
            clear()
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value).isEqualTo(paper)

            // Return Response.Error with DataNotFound exception
            val dataNotFound = repo.getData(paper.uid).last()
            Truth.assertThat(dataNotFound.isError()).isTrue()
            Truth.assertThat(dataNotFound.exception is PaperException.DataNotFound).isTrue()
        }
    }

    @Test
    fun getAllData() {
        runBlocking {
            // Return Response.Success
            val secondPaper = Paper(userUid)
            repo.create(paper).last()
            repo.create(secondPaper).last()
            val successResponse = repo.getAllData(userUid).take(2).last()
            clear()
            repo.delete(secondPaper).last()
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value).contains(paper)
            Truth.assertThat(successResponse.value).contains(secondPaper)

            // Return Response.Error with DataNotFound exception
            val dataNotFound = repo.getAllData(userUid).take(2).last()
            Truth.assertThat(dataNotFound.isError()).isTrue()
            Truth.assertThat(dataNotFound.exception is PaperException.DataNotFound).isTrue()
        }
    }

    private fun clear() {
        runBlocking { repo.delete(paper).last() }
    }

}
