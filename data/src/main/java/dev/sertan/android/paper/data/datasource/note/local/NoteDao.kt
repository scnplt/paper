package dev.sertan.android.paper.data.datasource.note.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.sertan.android.paper.data.datasource.note.NoteDataSource
import dev.sertan.android.paper.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NoteDao : NoteDataSource {

    @Query("SELECT * FROM NoteEntity WHERE userUid == :userUid")
    override fun getAllAsStream(userUid: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM NoteEntity WHERE uid == :noteUid")
    override suspend fun getNote(noteUid: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(vararg notes: NoteEntity)

    @Delete
    override suspend fun delete(note: NoteEntity)

    @Update
    override suspend fun update(note: NoteEntity)
}
