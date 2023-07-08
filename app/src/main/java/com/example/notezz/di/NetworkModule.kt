package com.example.notezz.di

import com.example.notezz.api.AuthApiService
import com.example.notezz.api.NoteApiService
import com.example.notezz.utils.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun providesAuthApiService(retrofit: Retrofit) : AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesNoteApiService(retrofit: Retrofit) : NoteApiService {
        return retrofit.create(NoteApiService::class.java)
    }

}