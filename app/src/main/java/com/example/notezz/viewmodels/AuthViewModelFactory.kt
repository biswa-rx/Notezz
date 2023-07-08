package com.example.notezz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.repository.AuthRepository
import javax.inject.Inject

class AuthViewModelFactory @Inject constructor(private val authViewModel: AuthViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return authViewModel as T
    }
}