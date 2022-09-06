package dev.sertan.android.paper.data.mapper

import dev.sertan.android.paper.data.model.DatabaseNote
import dev.sertan.android.paper.domain.model.Date
import dev.sertan.android.paper.domain.model.NoteDto

internal fun NoteDto.toDatabaseNote(): DatabaseNote {
    return DatabaseNote(
        uid = uid,
        userUid = userUid,
        title = title,
        content = content,
        createDateMillis = createDate.millisecond,
        updateDateMillis = updateDate.millisecond
    )
}

internal fun DatabaseNote?.toNoteDto(): NoteDto? {
    return this?.run {
        NoteDto(
            uid = uid,
            userUid = userUid,
            title = title.orEmpty(),
            content = content.orEmpty(),
            createDate = Date.fromMillisecond(createDateMillis),
            updateDate = Date.fromMillisecond(updateDateMillis)
        )
    }
}

internal fun List<DatabaseNote>.toNoteDtoList(): List<NoteDto> = mapNotNull { it.toNoteDto() }
