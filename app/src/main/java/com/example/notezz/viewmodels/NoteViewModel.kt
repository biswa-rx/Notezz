package com.example.notezz.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notezz.model.note_model.NoteModelDB
import com.example.notezz.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    val allNotes: LiveData<List<NoteModelDB>>
        get() = noteRepository.allNotes


    fun getAllNote() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllNote()
        }
    }

    fun createNote(noteName: String,noteText: String,color: String = "#FFFFFF"){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.createNote(noteName, noteText, color)
        }
    }
}