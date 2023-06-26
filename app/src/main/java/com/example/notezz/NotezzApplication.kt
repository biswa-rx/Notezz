package com.example.notezz

import android.app.Application
import com.example.notezz.api.AuthApiService
import com.example.notezz.api.RetrofitHelper
import com.example.notezz.repository.AuthRepository

class NotezzApplication : Application() {
    lateinit var authRepository: AuthRepository;
    override fun onCreate() {
        super.onCreate()
        initialize()
    }
    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(AuthApiService::class.java)
        authRepository = AuthRepository(quoteService, applicationContext)
    }
}