package com.example.notezz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notezz.model.note_model.ArchiveModelDB
import com.example.notezz.model.note_model.NoteModelDB

@Database(entities = [NoteModelDB::class, ArchiveModelDB::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun NoteDao() : NoteDao
    abstract fun ArchiveDao() : ArchiveDao

    companion object{
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        NoteDatabase::class.java,
                        "note_database")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}