package dev.sertan.android.paper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class NoteEntity(
    @PrimaryKey val uid: String = UUID.randomUUID().toString(),
    val userUid: String? = null,
    val title: String? = null,
    val content: String? = null,
    val createDateMillis: Long? = null,
    val updateDateMillis: Long? = null
)
