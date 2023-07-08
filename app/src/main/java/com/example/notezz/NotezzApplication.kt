package com.example.notezz

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.notezz.api.AuthApiService
import com.example.notezz.api.NoteApiService
import com.example.notezz.api.RetrofitHelper
import com.example.notezz.db.NoteDatabase
import com.example.notezz.di.ApplicationComponent
import com.example.notezz.di.DaggerApplicationComponent
import com.example.notezz.repository.AuthRepository
import com.example.notezz.repository.NoteRepository
import com.example.notezz.utils.AccessTokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class NotezzApplication : Application() {
//    lateinit var authRepository: AuthRepository;
//    lateinit var noteRepository: NoteRepository;
//    lateinit var sharedPreferences: SharedPreferences
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        initialize()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
//        AccessTokenManager.setRefreshToken(sharedPreferences.getString("refresh-token",null).toString())
    }
    private fun initialize() {
//        val authApiService = RetrofitHelper.getInstance().create(AuthApiService::class.java)
//        sharedPreferences = applicationContext.getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)
//        authRepository = AuthRepository(authApiService, sharedPreferences, applicationContext)
//
//        val noteApiService = RetrofitHelper.getInstance().create(NoteApiService::class.java)
//        val database = NoteDatabase.getDatabase(applicationContext)
//        noteRepository = NoteRepository(noteApiService,database,applicationContext)
    }
}