package dev.sertan.android.paper.data.db

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sertan.android.paper.data.model.Paper
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
class PaperDbServiceTest {
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
            val result = service.create(paper)
            service.delete(paper)
            Truth.assertThat(result.isSuccess()).isTrue()
        }
    }

    @Test
    fun delete() {
        runBlocking {
            // This data wasn't created.
            val step1 = service.delete(paper)
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is DbException.DataNotFound).isTrue()

            // Data created.
            service.create(paper)
            val step2 = service.delete(paper)
            Truth.assertThat(step2.isSuccess()).isTrue()
        }
    }

    @Test
    fun update() {
        runBlocking {
            // Data wasn't created.
            val step1 = service.update(paper)
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is DbException.DataNotFound).isTrue()

            // Data was created.
            service.create(paper)
            val step2 = service.update(paper.copy(title = "Updated"))
            service.delete(paper)
            Truth.assertThat(step2.isSuccess()).isTrue()
        }
    }

    @Test
    fun getData() {
        runBlocking {
            // Data wasn't created.
            val step1 = service.getData(paper.uid)
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is DbException.DataNotFound).isTrue()

            // Data was created.
            service.create(paper)
            val step2 = service.getData(paper.uid)
            service.delete(paper)
            Truth.assertThat(step2.isSuccess()).isTrue()
            Truth.assertThat(step2.value).isEqualTo(paper)
        }
    }

    @Test
    fun getAllData() {
        runBlocking {
            // No data in the database.
            val step1 = service.getAllData(userUid).first()
            Truth.assertThat(step1.isError()).isTrue()
            Truth.assertThat(step1.exception is DbException.DataNotFound).isTrue()

            // Data was created.
            val secondPaper = Paper(userUid)
            service.create(paper)
            service.create(secondPaper)
            val step2 = service.getAllData(userUid).first()
            service.delete(paper)
            service.delete(secondPaper)
            Truth.assertThat(step2.isSuccess()).isTrue()
            Truth.assertThat(step2.value).contains(paper)
            Truth.assertThat(step2.value).contains(secondPaper)
        }
    }
}
