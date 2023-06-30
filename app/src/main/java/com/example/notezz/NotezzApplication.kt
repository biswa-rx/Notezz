package com.example.notezz

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.notezz.api.AuthApiService
import com.example.notezz.api.NoteApiService
import com.example.notezz.api.RetrofitHelper
import com.example.notezz.db.NoteDatabase
import com.example.notezz.repository.AuthRepository
import com.example.notezz.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class NotezzApplication : Application() {
    lateinit var authRepository: AuthRepository;
    lateinit var noteRepository: NoteRepository;
    override fun onCreate() {
        super.onCreate()
        initialize()
    }
    private fun initialize() {
        val authApiService = RetrofitHelper.getInstance().create(AuthApiService::class.java)
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)
        authRepository = AuthRepository(authApiService, sharedPreferences, applicationContext)

        val noteApiService = RetrofitHelper.getInstance().create(NoteApiService::class.java)
        val database = NoteDatabase.getDatabase(applicationContext)
        noteRepository = NoteRepository(noteApiService,database,applicationContext)
    }
}