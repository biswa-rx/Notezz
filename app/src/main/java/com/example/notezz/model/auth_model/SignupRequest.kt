package com.example.notezz.model.auth_model

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)
