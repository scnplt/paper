package dev.sertan.android.paper.data.mapper

import dev.sertan.android.paper.data.model.NoteEntity
import dev.sertan.android.paper.domain.model.Date
import dev.sertan.android.paper.domain.model.NoteDto

internal fun NoteDto.toNoteEntity(): NoteEntity {
    return NoteEntity(
        uid = uid,
        userUid = userUid,
        title = title,
        content = content,
        createDateMillis = createDate.millisecond,
        updateDateMillis = updateDate.millisecond
    )
}

internal fun NoteEntity?.toNoteDto(): NoteDto? {
    return this?.run {
        NoteDto(
            uid = uid,
            userUid = userUid ?: return null,
            title = title.orEmpty(),
            content = content.orEmpty(),
            createDate = Date.fromMillisecond(createDateMillis ?: 0),
            updateDate = Date.fromMillisecond(updateDateMillis ?: 0)
        )
    }
}

internal fun List<NoteEntity>.toNoteDtoList(): List<NoteDto> = mapNotNull { it.toNoteDto() }
