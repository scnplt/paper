package dev.sertan.android.paper.domain.model

data class NoteDto(
    val uid: String,
    val userUid: String,
    val title: String,
    val content: String,
    val createDate: Date,
    val updateDate: Date
)
