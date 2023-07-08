package com.example.notezz.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Singleton
    @Provides
    fun getSharedPreferences(context : Context) : SharedPreferences{
        return context.getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)
    }
}