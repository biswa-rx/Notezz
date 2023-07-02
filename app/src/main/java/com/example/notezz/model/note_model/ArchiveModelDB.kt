package com.example.notezz.model.note_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archive_table")
data class ArchiveModelDB(
    @PrimaryKey(autoGenerate = true)
    var noteId: Int,
    var name: String,
    var text: String,
    var color: String,
    var isDeleted: Boolean,
)