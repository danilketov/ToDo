package com.danilketov.todo

import android.app.Application
import androidx.room.Room
import com.danilketov.todo.db.AppDatabase
import com.danilketov.todo.db.NoteDao

class App : Application() {

    private var db: AppDatabase? = null
    private var noteDao: NoteDao? = null

    companion object {
        private var INSTANCE: App? = null

        fun getInstance(): App? {
            return INSTANCE
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        getAppDatabase()
        noteDao = db?.repositoryDao()
    }

    private fun getAppDatabase() {
        db = Room.databaseBuilder<AppDatabase>(
            applicationContext,
            AppDatabase::class.java, AppDatabase.DB_NAME
        )
            .allowMainThreadQueries()
            .build()
    }

    fun getNoteDao(): NoteDao? {
        return noteDao
    }
}