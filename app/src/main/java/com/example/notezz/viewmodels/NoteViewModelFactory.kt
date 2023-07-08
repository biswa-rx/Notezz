package com.example.notezz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.repository.AuthRepository
import com.example.notezz.repository.NoteRepository
import javax.inject.Inject

class NoteViewModelFactory @Inject constructor(private val noteViewModel: NoteViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return noteViewModel as T
    }
}