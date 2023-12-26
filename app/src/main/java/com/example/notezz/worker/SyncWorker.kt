package com.example.notezz.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.work.SystemClock
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notezz.api.AuthApiService
import com.example.notezz.api.NoteApiService
import com.example.notezz.db.NoteDatabase
import com.example.notezz.repository.AuthRepository
import com.example.notezz.repository.NoteRepository
import com.example.notezz.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : Worker(appContext, workerParams) {

    lateinit var retrofit: Retrofit
    lateinit var noteApiService: NoteApiService
    lateinit var authApiService: AuthApiService
    lateinit var noteDatabase: NoteDatabase
    lateinit var authRepository: AuthRepository
    lateinit var sharedPreferences: SharedPreferences
    lateinit var noteRepository: NoteRepository
    override fun doWork(): Result {
        retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        noteApiService = retrofit.create(NoteApiService::class.java)
        authApiService = retrofit.create(AuthApiService::class.java)
        noteDatabase = Room.databaseBuilder(applicationContext, NoteDatabase::class.java,"note_database").build()
        sharedPreferences = applicationContext.getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)

        authRepository = AuthRepository(authApiService,sharedPreferences,applicationContext)
        noteRepository = NoteRepository(noteApiService,noteDatabase,authRepository,applicationContext)

        //Hybrid sync
        CoroutineScope(Dispatchers.IO).launch {
            noteRepository.syncData()
            delay(3000)
            noteRepository.syncAllData()
        }

        return Result.success()
    }
}