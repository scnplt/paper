package dev.sertan.android.paper.data.mapper

import dev.sertan.android.paper.data.model.DatabaseNote
import dev.sertan.android.paper.domain.model.Date
import dev.sertan.android.paper.domain.model.NoteDto
import java.util.UUID
import org.junit.Test

internal class NoteMapperTest {

    @Test
    fun `noteDto to databaseNote`() {
        val noteDto = NoteDto(
            uid = UUID.randomUUID().toString(),
            userUid = UUID.randomUUID().toString(),
            title = "",
            content = "",
            createDate = Date.fromMillisecond(0),
            updateDate = Date.fromMillisecond(0)
        )
        val databaseNote = noteDto.toDatabaseNote()

        assert(databaseNote.uid == noteDto.uid)
        assert(databaseNote.userUid == noteDto.userUid)
        assert(databaseNote.title == noteDto.title)
        assert(databaseNote.content == noteDto.content)
        assert(databaseNote.createDateMillis == noteDto.createDate.millisecond)
        assert(databaseNote.updateDateMillis == noteDto.updateDate.millisecond)
    }

    @Test
    fun `databaseNote to noteDto`() {
        val databaseNote = DatabaseNote(
            uid = UUID.randomUUID().toString(),
            userUid = UUID.randomUUID().toString(),
            title = null,
            content = null,
            createDateMillis = 0,
            updateDateMillis = 0
        )
        val noteDto = databaseNote.toNoteDto()

        assert(noteDto?.uid == databaseNote.uid)
        assert(noteDto?.userUid == databaseNote.userUid)
        assert(noteDto?.title?.isBlank() == true)
        assert(noteDto?.content?.isBlank() == true)
        assert(noteDto?.createDate?.millisecond == databaseNote.createDateMillis)
        assert(noteDto?.updateDate?.millisecond == databaseNote.updateDateMillis)
    }

    @Test
    fun `databaseNoteList to noteDtoList`() {
        val databaseNoteList = (0..2).map {
            DatabaseNote(
                uid = UUID.randomUUID().toString(),
                userUid = UUID.randomUUID().toString(),
                title = it.toString(),
                content = it.toString(),
                createDateMillis = it.toLong(),
                updateDateMillis = 0
            )
        }
        val noteDtoList = databaseNoteList.toNoteDtoList()

        assert(noteDtoList.size == databaseNoteList.size)
        assert(noteDtoList == databaseNoteList.map { it.toNoteDto() })
    }
}
