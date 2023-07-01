package com.example.notezz.model.note_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteModelDB(
    @PrimaryKey(autoGenerate = true)
    var noteId: Int,
    var id: String,
    var userId: String,
    var name: String,
    var text: String,
    var color: String = "#FFFF00",
    var isDeleted: Boolean,
    var isCreated: Boolean,
    var isUpdated: Boolean,
)
