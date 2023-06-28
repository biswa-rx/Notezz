package com.example.notezz

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.notezz.api.AuthApiService
import com.example.notezz.api.RetrofitHelper
import com.example.notezz.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class NotezzApplication : Application() {
    lateinit var authRepository: AuthRepository;
    override fun onCreate() {
        super.onCreate()
        initialize()
    }
    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(AuthApiService::class.java)
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)
        authRepository = AuthRepository(quoteService, sharedPreferences, applicationContext)
    }
}