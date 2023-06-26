package com.example.notezz.api

import com.example.notezz.model.LoginRequest
import com.example.notezz.model.LoginResponse
import com.example.notezz.model.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/resister")
    suspend fun resister(@Body signupRequest: SignupRequest): Response<LoginResponse>

}