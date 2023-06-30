package com.example.notezz.model.note_model

data class UpdateNoteModel(
    val accessToken: String,
    val name: String,
    val text: String
)
