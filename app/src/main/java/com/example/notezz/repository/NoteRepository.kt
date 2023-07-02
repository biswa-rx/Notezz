package com.example.notezz.repository

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notezz.NotezzApplication
import com.example.notezz.api.NoteApiService
import com.example.notezz.db.NoteDatabase
import com.example.notezz.model.note_model.AccessTokenBody
import com.example.notezz.model.note_model.ArchiveModelDB
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

    private val _archiveNotesLiveData = MutableLiveData<List<ArchiveModelDB>>();

    val allNotes: LiveData<List<NoteModelDB>>
        get() = _notesLiveData

    val allArchiveNotes: LiveData<List<ArchiveModelDB>>
        get() = _archiveNotesLiveData

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
        noteDatabase.NoteDao().delete(note)
    }

    //when ever user welcome as Login
    suspend fun syncAllData() {
        try {
            if (!NetworkUtils.isInternetAvailable(applicationContext)) {
                return
            }else {
                if (AccessTokenManager.getAccessToken().isEmpty()) {
                    (applicationContext as NotezzApplication).authRepository.authorizeUser()
                }
            }
            val response =
                noteApiService.fetchAllNote(
                    "Bearer " + AccessTokenManager.getAccessToken().toString()
                )
            if (response.isSuccessful) {
                val notesResponse = response.body()
                if (notesResponse != null) {
//                    CustomToast.makeToast(applicationContext, notesResponse.toString())
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
                    getAllNote()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, (e.message ?: "Login failed"))
        }
    }

    //User welcome as Refresh Token <implimented by job sheduler>
    suspend fun syncData() {
        if (!NetworkUtils.isInternetAvailable(applicationContext)) {
            return
        }
        if (AccessTokenManager.getAccessToken().isEmpty()) {
            (applicationContext as NotezzApplication).authRepository.authorizeUser()
        }
        val noteModelDbList = noteDatabase.NoteDao().getNotes()
        for (noteModelDB in noteModelDbList) {
            if (noteModelDB.isDeleted and noteModelDB.isCreated) {
                noteDatabase.NoteDao().delete(noteModelDB)
                continue
            } else if (noteModelDB.isDeleted and !noteModelDB.isCreated) {
                val response = noteApiService.deleteNote(
                    noteModelDB.id,
                    "Bearer " + AccessTokenManager.getAccessToken().toString()
                )
                noteDatabase.NoteDao().delete(noteModelDB)
            } else if (noteModelDB.isCreated) {
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
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //Archive note
    suspend fun getAllArchiveNote() {
        val notes = noteDatabase.ArchiveDao().getNotes()
        _archiveNotesLiveData.postValue(notes)
    }

    suspend fun createArchiveNote(note: ArchiveModelDB) {
        noteDatabase.ArchiveDao().addNote(note)
    }

    suspend fun updateArchiveNote(note: ArchiveModelDB) {
        noteDatabase.ArchiveDao().update(note)
    }

    suspend fun temporaryDeleteArchiveNote(note: ArchiveModelDB) {
        note.isDeleted = true
        noteDatabase.ArchiveDao().update(note)
        println("Temporary deleted called")
    }

    suspend fun deleteArchiveNote(note: ArchiveModelDB) {
        noteDatabase.ArchiveDao().delete(note)
        println("permanent deleted called")
    }
}
