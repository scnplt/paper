package dev.sertan.android.paper.data.db

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.data.model.Paper
import dev.sertan.android.paper.data.util.PaperException
import javax.inject.Inject
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
internal class PaperDbServiceTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var service: DbService<Paper>

    private val userUid = "1"
    private val paper = Paper(userUid)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun create() {
        runBlocking {
            // Return Response.Success
            val successResponse = service.create(paper)
            service.delete(paper)
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value).isEqualTo(paper)
        }
    }

    @Test
    fun delete() {
        runBlocking {
            // Return Response.Error with DataNotFound exception
            val errorResponse = service.delete(paper)
            Truth.assertThat(errorResponse.isError()).isTrue()
            Truth.assertThat(errorResponse.exception is PaperException.DataNotFound).isTrue()

            // Return Response.Success
            service.create(paper)
            val successResponse = service.delete(paper)
            Truth.assertThat(successResponse.isSuccess()).isTrue()
        }
    }

    @Test
    fun update() {
        runBlocking {
            // Return Response.Error with DataNotFound exception
            val errorResponse = service.update(paper)
            Truth.assertThat(errorResponse.isError()).isTrue()
            Truth.assertThat(errorResponse.exception is PaperException.DataNotFound).isTrue()

            // Return Response.Success
            service.create(paper)
            val successResponse = service.update(paper.copy(title = "Updated"))
            service.delete(paper)
            Truth.assertThat(successResponse.isSuccess()).isTrue()
        }
    }

    @Test
    fun getData() {
        runBlocking {
            // Return Response.Error with DataNotFound exception
            val errorResponse = service.getData(paper.uid)
            Truth.assertThat(errorResponse.isError()).isTrue()
            Truth.assertThat(errorResponse.exception is PaperException.DataNotFound).isTrue()

            // Return Response.Success
            service.create(paper)
            val successResponse = service.getData(paper.uid)
            service.delete(paper)
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value).isEqualTo(paper)
        }
    }

    @Test
    fun getAllData() {
        runBlocking {
            // Return Response.Error with DataNotFound exception
            val errorResponse = service.getAllData(userUid).take(2).last()
            Truth.assertThat(errorResponse.isError()).isTrue()
            Truth.assertThat(errorResponse.exception is PaperException.DataNotFound).isTrue()

            // Return Response.Success
            val secondPaper = Paper(userUid)
            service.create(paper)
            service.create(secondPaper)
            val successResponse = service.getAllData(userUid).take(2).last()
            service.delete(paper)
            service.delete(secondPaper)
            Truth.assertThat(successResponse.isSuccess()).isTrue()
            Truth.assertThat(successResponse.value).contains(paper)
            Truth.assertThat(successResponse.value).contains(secondPaper)
        }
    }
}
