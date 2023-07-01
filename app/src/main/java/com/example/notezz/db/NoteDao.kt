package com.example.notezz.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notezz.model.note_model.NoteModelDB

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes_table")
    suspend fun getNotes() : List<NoteModelDB>
    @Insert
    suspend fun addNotes(notes: List<NoteModelDB>)
    @Insert
    suspend fun addNote(notes: NoteModelDB)
    @Update
    suspend fun update(note: NoteModelDB)
    @Delete
    suspend fun delete(note: NoteModelDB)
    @Query("DELETE FROM notes_table")
    suspend fun clearTable()
}