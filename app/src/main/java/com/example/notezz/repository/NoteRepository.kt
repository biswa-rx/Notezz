package com.example.notezz.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notezz.api.NoteApiService
import com.example.notezz.db.NoteDatabase
import com.example.notezz.model.note_model.NoteModelDB

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
    suspend fun createNote(noteName: String,noteText: String,color: String){
        val note = NoteModelDB(0,"","",noteName,noteText,color,false,true,false);
        noteDatabase.NoteDao().addNote(note)
    }

}