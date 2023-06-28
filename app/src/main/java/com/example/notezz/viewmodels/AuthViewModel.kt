package com.example.notezz.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notezz.model.ErrorResponse
import com.example.notezz.model.AuthorizationResponse
import com.example.notezz.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    companion object{
        var verifiedSession = false;
    }
    init {
        if(!verifiedSession) {
            viewModelScope.launch(Dispatchers.IO) {
                authRepository.authorizeUser()
            }
            verifiedSession = true
        }
    }
    val accessCode : LiveData<AuthorizationResponse>
    get() = authRepository.accessCode

    val errorMessage : LiveData<ErrorResponse>
    get() = authRepository.errorMessage

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

    fun authorize() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.authorizeUser()
        }
    }
}