package com.example.notezz.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notezz.model.LoginResponse
import com.example.notezz.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val accessCode : LiveData<LoginResponse>
    get() = authRepository.accessCode

    fun resister(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.resisterUser(name, email, password)
        }
    }
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginUser(email, password)
        }
    }
}