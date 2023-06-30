package com.example.notezz.model.note_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteModelDB(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int,
    val id: String,
    val userId: String,
    val name: String,
    val text: String,
    val color: String,
    val isDeleted: Boolean,
    val isCreated: Boolean,
    val isUpdated: Boolean,
)
