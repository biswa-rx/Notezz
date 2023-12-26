package com.example.notezz.model.note_model

data class CreateNoteModel(
    val accessToken: String,
    val name: String,
    val text: String,
    val color: String
)
