package dev.sertan.android.paper.data.datasource.note.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.sertan.android.paper.data.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
internal abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
