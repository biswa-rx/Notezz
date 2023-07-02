package com.example.notezz.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.notezz.model.note_model.ArchiveModelDB
import com.example.notezz.model.note_model.NoteModelDB

@Dao
interface ArchiveDao {
    @Query("SELECT * FROM archive_table")
    suspend fun getNotes() : List<ArchiveModelDB>
    @Insert
    suspend fun addNotes(notes: List<ArchiveModelDB>)
    @Insert
    suspend fun addNote(notes: ArchiveModelDB)
    @Update
    @Transaction
    suspend fun update(note: ArchiveModelDB)
    @Delete
    suspend fun delete(note: ArchiveModelDB)
    @Query("DELETE FROM archive_table")
    suspend fun clearTable()
}