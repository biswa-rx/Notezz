package com.example.notezz.api

import com.example.notezz.model.note_model.AccessTokenBody
import com.example.notezz.model.note_model.CreateNoteModel
import com.example.notezz.model.note_model.NoteResponse
import com.example.notezz.model.note_model.UpdateNoteModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApiService {
    // All note
    @GET("notes")
    suspend fun fetchAllNote(@Header("Authorization") token: String): Response<List<NoteResponse>>

    // Find note by id
    @POST("notes/{id}")
    suspend fun fetchNoteById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<NoteResponse>

    // Create new note
    @POST("notes")
    suspend fun createNewNote(@Body createNoteModel: CreateNoteModel): Response<NoteResponse>

    // update existing note
    @PATCH("notes/{id}")
    suspend fun updateNote(
        @Path("id") id: String,
        @Body updateNoteModel: UpdateNoteModel
    ): Response<NoteResponse>

    // Delete note
    @DELETE("notes/{id}")
    suspend fun deleteNote(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<NoteResponse>
}