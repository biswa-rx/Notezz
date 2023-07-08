package com.example.notezz.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notezz.model.note_model.ArchiveModelDB
import com.example.notezz.model.note_model.NoteModelDB
import com.example.notezz.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    val allNotes: LiveData<List<NoteModelDB>>
        get() = noteRepository.allNotes

    val allArchiveNotes: LiveData<List<ArchiveModelDB>>
        get() = noteRepository.allArchiveNotes

    fun getAllNote() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllNote()
        }
    }

    fun createNote(noteName: String,noteText: String,color: String = "#FFFFFF"){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.createNote(noteName, noteText, color)
            noteRepository.getAllNote()
        }
    }
    fun updateNote(note: NoteModelDB){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(note)
            noteRepository.getAllNote()
        }
    }
    fun deletePendingNote(note: NoteModelDB){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deletePendingNote(note)
            noteRepository.getAllNote()
        }
    }
    fun deleteNote(note: NoteModelDB){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)
        }
    }

    fun syncAllNote(){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.syncAllData()
        }
    }

    fun syncNote(){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.syncData()
        }
    }

    fun hybridSync(){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.syncData()
            noteRepository.syncAllData()
        }
    }

    fun getAllArchiveNote(){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllArchiveNote()
        }
    }
    fun createArchiveNote(note: ArchiveModelDB){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.createArchiveNote(note)
            noteRepository.getAllArchiveNote()
        }
    }

    fun updateArchiveNote(note: ArchiveModelDB){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateArchiveNote(note)
            noteRepository.getAllArchiveNote()
        }
    }
    fun deleteWaitingArchiveNote(note: ArchiveModelDB){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.temporaryDeleteArchiveNote(note)
            noteRepository.getAllArchiveNote()
        }
    }
    fun deleteArchiveNote(note: ArchiveModelDB){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteArchiveNote(note)
            noteRepository.getAllArchiveNote()
        }
    }

}