package com.example.notezz.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notezz.api.AuthApiService
import com.example.notezz.model.auth_model.AuthorizationRequest
import com.example.notezz.model.auth_model.ErrorResponse
import com.example.notezz.model.auth_model.LoginRequest
import com.example.notezz.model.auth_model.AuthorizationResponse
import com.example.notezz.model.auth_model.ErrorData
import com.example.notezz.model.auth_model.SignupRequest
import com.google.gson.Gson

class AuthRepository(
    private val authApiService: AuthApiService,
    private val sharedPreferences: SharedPreferences,
    private val applicationContext: Context) {
    private val TAG = "AuthRepository"
    private val accessCodeLiveData = MutableLiveData<AuthorizationResponse>();
    private val _errorMessage = MutableLiveData<ErrorResponse>();
    val accessCode: LiveData<AuthorizationResponse>
    get() = accessCodeLiveData

    val errorMessage: LiveData<ErrorResponse>
    get() = _errorMessage


    suspend fun resisterUser(name: String, email: String, password: String) {
        try {
            val signupRequest = SignupRequest(name, email, password)
            val response = authApiService.resister(signupRequest)

            if (response.isSuccessful) {
                val authorizationResponse = response.body()
                val editor = sharedPreferences.edit()
                if (authorizationResponse != null) {
                    editor.putString("refresh-token", authorizationResponse.REFRESH_TOKEN)
                    editor.apply()
                    accessCodeLiveData.postValue(authorizationResponse!!)
                }
            }else {
                val gson = Gson();
                val errorResponse = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java);
                _errorMessage.postValue(errorResponse);
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
                val authorizationResponse = response.body()
                val editor = sharedPreferences.edit()
                if (authorizationResponse != null) {
                    editor.putString("refresh-token", authorizationResponse.REFRESH_TOKEN)
                    editor.apply()
                    accessCodeLiveData.postValue(authorizationResponse!!)
                }
            } else {
                val gson = Gson();
                val errorResponse = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java);
                _errorMessage.postValue(errorResponse);
            }
        } catch (e: Exception) {
            Log.e(TAG,(e.message ?: "Login failed"))
        }
    }

    suspend fun authorizeUser() {
        try {
            val refreshToken = sharedPreferences.getString("refresh-token",null);
            if(refreshToken == null) {
                val errorResponse = ErrorResponse(ErrorData(401,"Please resister your self"))
                _errorMessage.postValue(errorResponse)
                return
            }
            val authorizationRequest = AuthorizationRequest(refreshToken);
            val response = authApiService.authorise(authorizationRequest)

            if (response.isSuccessful) {
                val authorizationResponse = response.body()
                val editor = sharedPreferences.edit()
                if (authorizationResponse != null) {
                    editor.putString("refresh-token", authorizationResponse.REFRESH_TOKEN)
                    editor.apply()
                    accessCodeLiveData.postValue(authorizationResponse!!)
                }
            } else {
                val gson = Gson();
                val errorResponse = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java);
                errorResponse.error.message += "\nAnother device login detected"
                _errorMessage.postValue(errorResponse);
                val editor = sharedPreferences.edit()
                editor.putString("refresh-token", null)
                editor.apply()
            }
        } catch (e: Exception) {
            Log.e(TAG,(e.message ?: "Login failed"))
        }
    }
}