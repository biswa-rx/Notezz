package com.example.notezz.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notezz.api.AuthApiService
import com.example.notezz.model.ErrorData
import com.example.notezz.model.ErrorResponse
import com.example.notezz.model.LoginRequest
import com.example.notezz.model.LoginResponse
import com.example.notezz.model.SignupRequest

class AuthRepository(private val authApiService: AuthApiService, private val applicationContext: Context) {
    private val TAG = "AuthRepository"
    private val accessCodeLiveData = MutableLiveData<LoginResponse>();

    val accessCode: LiveData<LoginResponse>
    get() = accessCodeLiveData

    private val _errorMessage = MutableLiveData<ErrorResponse>();

    val errorMessage: LiveData<ErrorResponse>
    get() = _errorMessage


    suspend fun resisterUser(name: String, email: String, password: String) {
        try {
            val signupRequest = SignupRequest(name, email, password)
            val response = authApiService.resister(signupRequest)

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    accessCodeLiveData.postValue(loginResponse!!)
                }
            }else {
                Handler(Looper.getMainLooper()).post(Runnable {
                    Toast.makeText(applicationContext,"Authentication Failed \n" +
                            "Response code: "+response.code()+
                            "\nResponse message: "+response.message(),Toast.LENGTH_LONG).show();
                })
            }
        } catch (e: Exception) {
            Log.e(TAG, "resisterUser: "+(e.message ?: "Login failed"))
        }
    }
    suspend fun loginUser(email: String, password: String) {
        try {
            val loginRequest = LoginRequest(email, password)
            val response = authApiService.login(loginRequest)

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    accessCodeLiveData.postValue(loginResponse!!)
                }
            } else {
                Handler(Looper.getMainLooper()).post(Runnable {
                    Toast.makeText(applicationContext,"Authentication Failed \n" +
                            "Response code: "+response.code()+
                            "\nResponse message: "+response.message(),Toast.LENGTH_LONG).show();
                })
            }
        } catch (e: Exception) {
            Log.e(TAG,(e.message ?: "Login failed"))
        }
    }
}