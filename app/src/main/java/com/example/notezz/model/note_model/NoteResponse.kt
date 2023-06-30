package com.example.notezz.model.note_model

import com.google.gson.annotations.SerializedName

data class NoteResponse(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val name: String,
    val text: String
    )
