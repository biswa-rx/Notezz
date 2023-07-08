package com.example.notezz.di

import android.content.Context
import androidx.room.Room
import com.example.notezz.db.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(context : Context) : NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java,"note_database").build()
    }
}