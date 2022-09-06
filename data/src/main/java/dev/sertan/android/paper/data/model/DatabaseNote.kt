package dev.sertan.android.paper.data.model

data class DatabaseNote(
    val uid: String,
    val userUid: String,
    val title: String?,
    val content: String?,
    val createDateMillis: Long,
    val updateDateMillis: Long
)
