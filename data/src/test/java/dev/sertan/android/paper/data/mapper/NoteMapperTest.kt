package dev.sertan.android.paper.data.mapper

import dev.sertan.android.paper.data.model.NoteEntity
import dev.sertan.android.paper.domain.model.Date
import dev.sertan.android.paper.domain.model.NoteDto
import java.util.UUID
import org.junit.Test

internal class NoteMapperTest {

    @Test
    fun `noteDto to noteEntity`() {
        val noteDto = NoteDto(
            uid = UUID.randomUUID().toString(),
            userUid = UUID.randomUUID().toString(),
            title = "",
            content = "",
            createDate = Date.fromMillisecond(0),
            updateDate = Date.fromMillisecond(0)
        )

        val noteEntity = noteDto.toNoteEntity()
        assert(noteEntity.uid == noteDto.uid)
        assert(noteEntity.userUid == noteDto.userUid)
        assert(noteEntity.title == noteDto.title)
        assert(noteEntity.content == noteDto.content)
        assert(noteEntity.createDateMillis == noteDto.createDate.millisecond)
        assert(noteEntity.updateDateMillis == noteDto.updateDate.millisecond)
    }

    @Test
    fun `noteEntity to noteDto`() {
        val noteEntity = NoteEntity(
            uid = UUID.randomUUID().toString(),
            userUid = UUID.randomUUID().toString(),
            title = null,
            content = null,
            createDateMillis = 0,
            updateDateMillis = 0
        )

        val noteDto = noteEntity.toNoteDto()
        assert(noteDto?.uid == noteEntity.uid)
        assert(noteDto?.userUid == noteEntity.userUid)
        assert(noteDto?.title?.isBlank() == true)
        assert(noteDto?.content?.isBlank() == true)
        assert(noteDto?.createDate?.millisecond == noteEntity.createDateMillis)
        assert(noteDto?.updateDate?.millisecond == noteEntity.updateDateMillis)
    }

    @Test
    fun `noteEntityList to noteDtoList`() {
        val noteEntityList = (0..2).map {
            NoteEntity(
                uid = UUID.randomUUID().toString(),
                userUid = UUID.randomUUID().toString(),
                title = it.toString(),
                content = it.toString(),
                createDateMillis = it.toLong(),
                updateDateMillis = 0
            )
        }

        val noteDtoList = noteEntityList.toNoteDtoList()
        assert(noteDtoList.size == noteEntityList.size)
        assert(noteDtoList == noteEntityList.map { it.toNoteDto() })
    }
}
