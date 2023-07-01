package com.example.notezz.repository

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notezz.api.NoteApiService
import com.example.notezz.db.NoteDatabase
import com.example.notezz.model.note_model.AccessTokenBody
import com.example.notezz.model.note_model.CreateNoteModel
import com.example.notezz.model.note_model.NoteModelDB
import com.example.notezz.model.note_model.UpdateNoteModel
import com.example.notezz.utils.AccessTokenManager
import com.example.notezz.utils.CustomToast
import com.example.notezz.utils.NetworkUtils
import java.util.logging.Handler

class NoteRepository(
    private val noteApiService: NoteApiService,
    private val noteDatabase: NoteDatabase,
    private val applicationContext: Context
) {

    private val TAG = "NoteRepository"
    private val _notesLiveData = MutableLiveData<List<NoteModelDB>>();

    val allNotes: LiveData<List<NoteModelDB>>
        get() = _notesLiveData

    suspend fun getAllNote() {
        val notes = noteDatabase.NoteDao().getNotes()
        _notesLiveData.postValue(notes)
    }

    suspend fun createNote(noteName: String, noteText: String, color: String) {
        val note = NoteModelDB(0, "", "", noteName, noteText, color, false, true, false);
        noteDatabase.NoteDao().addNote(note)
    }

    suspend fun updateNote(note: NoteModelDB) {
        note.isUpdated = true
        noteDatabase.NoteDao().update(note)
    }

    suspend fun deletePendingNote(note: NoteModelDB) {
        note.isDeleted = true
        noteDatabase.NoteDao().update(note)
    }

    suspend fun deleteNote(note: NoteModelDB) {
        noteDatabase.NoteDao().update(note)
    }

    //when ever user welcome as Login
    suspend fun syncAllData() {
        try {
            if (!NetworkUtils.isInternetAvailable(applicationContext)) {
                CustomToast.makeToast(applicationContext, "No internet")
                return
            }
            if (AccessTokenManager.getAccessToken() == null) {
                CustomToast.makeToast(
                    applicationContext,
                    "I have to request for access token in this portion"
                )
                return
            }
            val response =
                noteApiService.fetchAllNote(
                    "Bearer " + AccessTokenManager.getAccessToken().toString()
                )
            if (response.isSuccessful) {
                val notesResponse = response.body()
                if (notesResponse != null) {
                    CustomToast.makeToast(applicationContext, notesResponse.toString())
                    noteDatabase.NoteDao().clearTable()
                    val noteModelDBList = notesResponse.map { it ->
                        NoteModelDB(
                            0,
                            it.id,
                            it.userId,
                            it.name,
                            it.text,
                            "#FFFFFF",
                            false,
                            false,
                            false
                        );
                    }
                    noteDatabase.NoteDao().addNotes(noteModelDBList)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, (e.message ?: "Login failed"))
        }
    }

    //User welcome as Refresh Token <implimented by job sheduler>
    suspend fun syncData() {
        if (!NetworkUtils.isInternetAvailable(applicationContext)) {
            CustomToast.makeToast(applicationContext, "No internet")
            return
        }
        if (AccessTokenManager.getAccessToken() == null) {
            CustomToast.makeToast(
                applicationContext,
                "I have to request for access token in this portion"
            )
            return
        }
        val noteModelDbList = noteDatabase.NoteDao().getNotes()
        for (noteModelDB in noteModelDbList) {
            if (noteModelDB.isDeleted and noteModelDB.isCreated) {
                println(1)
                noteDatabase.NoteDao().delete(noteModelDB)
                continue
            } else if (noteModelDB.isDeleted and !noteModelDB.isCreated) {
                println(2)
                val response = noteApiService.deleteNote(
                    noteModelDB.id,
                    "Bearer " + AccessTokenManager.getAccessToken().toString()
                )
                noteDatabase.NoteDao().delete(noteModelDB)
            } else if (noteModelDB.isCreated) {
                println(3)
                val createNoteModel = CreateNoteModel(
                    AccessTokenManager.getAccessToken().toString(),
                    noteModelDB.name,
                    noteModelDB.text
                )
                val response = noteApiService.createNewNote(createNoteModel)
                if (response.isSuccessful) {
                    val noteResponse = response.body()
                    if (noteResponse != null) {
                        noteDatabase.NoteDao().delete(noteModelDB)
                        noteDatabase.NoteDao().addNote(
                            NoteModelDB(
                                0,
                                noteResponse.id,
                                noteResponse.userId,
                                noteResponse.name,
                                noteResponse.text,
                                "#FFFFFF",
                                false,
                                false,
                                false
                            )
                        )
                    }
                }
            } else if (noteModelDB.isUpdated) {
                println(4)
                val updateNoteModel = UpdateNoteModel(
                    AccessTokenManager.getAccessToken().toString(),
                    noteModelDB.name,
                    noteModelDB.text
                )
                val updateResponse = noteApiService.updateNote(noteModelDB.id, updateNoteModel)
                if (updateResponse.isSuccessful) {
                    val noteResponse = updateResponse.body()
                    if (noteResponse != null) {
                        noteDatabase.NoteDao().delete(noteModelDB)
                        noteDatabase.NoteDao().addNote(
                            NoteModelDB(
                                0,
                                noteResponse.id,
                                noteResponse.userId,
                                noteResponse.name,
                                noteResponse.text,
                                "#FFFFFF",
                                false,
                                false,
                                false
                            )
                        )
                    }
                }

            }
            println(5)
        }

    }
}
